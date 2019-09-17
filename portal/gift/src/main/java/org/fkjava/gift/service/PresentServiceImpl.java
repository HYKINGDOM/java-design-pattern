package org.fkjava.gift.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.gift.domain.Present;
import org.fkjava.gift.domain.Tally;
import org.fkjava.gift.repository.PresentRepository;
import org.fkjava.gift.repository.TallyRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PresentServiceImpl implements PresentService {


    private final TallyRepository tallyRepository;

    private final PresentRepository presentRepository;

    public PresentServiceImpl(TallyRepository tallyRepository, PresentRepository presentRepository) {
        this.tallyRepository = tallyRepository;
        this.presentRepository = presentRepository;
    }

    @Override
    public List<Tally> findAllTags() {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        return tallyRepository.findAll(sort);
    }

    @Override
    public Page<Present> findPresent(Integer pageNumber, String keyword, List<String> tallyId) {

        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Present> page;
        if (StringUtils.isEmpty(keyword) && (tallyId == null || tallyId.isEmpty())) {
            page = this.presentRepository.findAll(pageable);
        } else {
            Specification<Present> spec = (root, query, builder) -> {

                query.distinct(true);
                List<Predicate> predicates = new LinkedList<>();
                if (!StringUtils.isEmpty(keyword)) {
                    Predicate nameLike = builder.like(root.get("name"), "%" + keyword + "%");
                    predicates.add(nameLike);
                }
                if (tallyId != null && !tallyId.isEmpty()) {
                    //List<Tally> tally = this.tallyRepository.findAllById(tallyId);
                   // Predicate tallyIn = root.join("tally").in(tally);
                    predicates.add(root.join("tally").get("id").in(tallyId));
                }
                if (predicates.size() == 1) {
                    return predicates.get(0);
                } else {
                    return builder.and(predicates.toArray(new Predicate[0]));
                }
            };
            page = this.presentRepository.findAll(spec, pageable);
        }
        // 当某些字段不需要返回到页面的时候，往往就是新建一个Page对象，然后把需要的属性自己一个个的放入新对象里面
        List<Present> newContent = page.getContent().stream()
                .map(present -> new Present(
                        present.getId(),
                        present.getName(),
                        present.getPrice(),
                        null,
                        null,
                        present.getGift_describe(),
                        present.getGift_type()
                ))
                .collect(Collectors.toList());

        return (Page<Present>) new PageImpl(newContent, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public Result savePresent(Present present) {
        List<String> labels = present.getLabels();
        List<Tally> tally = this.tallyRepository.findByNameIn(labels);
// HashSet底层就是HashMap，只是底层存储的时候value永远是固定的
        Set<String> existsNames = tally.stream()
                .map(Tally::getName)// 只要标签的名称
                .collect(Collectors.toSet());

        Set<String> newTagNames = labels.stream()
                // 找到不存在的新标签名称
                .filter(name -> !existsNames.contains(name))
                .collect(Collectors.toSet());

        List<Tally> allTags = new LinkedList<>(tally);

        // 把新的标签存储到数据库
        newTagNames.forEach(name -> {
            Tally newTally = new Tally();
            newTally.setName(name);
            this.tallyRepository.save(newTally);
            allTags.add(newTally);
        });

        present.setTally(allTags);

        this.presentRepository.save(present);

        return Result.ok("内容保存成功");
    }

    public Present findPresentById(String id) {
        return this.presentRepository.findById(id).orElse(null);
    }

//    @Transactional
//    @Override
//    public Result delere(String id) {
//        this.presentRepository.findById(id)
//                .ifPresent(present -> {
//
//                });
//        return null;
//    }
}
