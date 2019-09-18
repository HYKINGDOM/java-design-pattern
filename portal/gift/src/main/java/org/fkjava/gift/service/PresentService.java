package org.fkjava.gift.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.gift.domain.Present;
import org.fkjava.gift.domain.Tally;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public interface PresentService {

    List<Tally> findAllTags();

    Page<Present> findPresent(Integer pageNumber, String keyword, List<String> tallyId);

    Result savePresent(Present present);

    Present findPresentById(String id);

//    Result delere(String id);
}
