<template>
  <div id="app">
    <Layout :headerFixed="headerFixed" :siderCollapsed="siderCollapsed">
      <Sider theme="dark">
        <div class="layout-logo">
          <router-link to="/">
            <img v-if="!siderCollapsed" class="logo" src="../assets/logo.png" />
          </router-link>
          <span class="title">
            <router-link to="/">
              <span v-if="!siderCollapsed">超级门户</span>
            </router-link>
            <Button
              :icon="siderCollapsed ? 'icon-align-right':'icon-align-left'"
              size="l"
              noBorder
              class="sider-collapse font20"
              @click="siderCollapsed=!siderCollapsed"
            ></Button>
          </span>
        </div>
        <Menu :datas="menuDatas" :inlineCollapsed="siderCollapsed" @click="trigger" ref="leftMenu"></Menu>
      </Sider>
      <Layout :siderFixed="siderFixed">
        <HHeader theme="white">
          <div>
            <UserInfo />
          </div>
        </HHeader>
        <Content style="padding-left: 30px; padding-top: 5px; padding-right: 30px;">
          <Breadcrumb :datas="breadcrumbDatas"></Breadcrumb>
          <router-view />
        </Content>

        <HFooter class="text-center">
          Copyright © 2019
          <a href="http://www.fkjava.org" target="_blank">疯狂软件</a>
        </HFooter>
      </Layout>
    </Layout>
  </div>
</template>

<script>
import menuData from "../menu.js";
import UserInfo from "./UserInfo";
export default {
  data() {
    return {
      headerFixed: false,
      siderFixed: false,
      breadcrumbDatas: [],
      menuDatas: [...menuData]
    };
  },
  components: { UserInfo },
  watch: {
    headerFixed() {
      if (!this.headerFixed) {
        this.siderFixed = false;
      }
    }
  },
  mounted() {
    // 挂载页面以后，根据路由的名称选中当前的菜单项目
    this.menuSelect();
  },
  watch: {
    // 监控路由发生变化的时候，自动选中菜单项目
    $route() {
      this.menuSelect();
    }
  },
  methods: {
    menuSelect() {
      if (this.$route.path) {
        // 选中菜单项目的时候，是根据路由的name来选中的，意味着name要跟菜单的key相同
        // $refs是一个Vue对象里面的共享对象，可以把组件通过共享对象在多个不同的组件之间传递
        // 只要在组件什么使用ref='menu'（menu可变），就会把组件注册到共享对象里面
        let matchedMenu = this.matchedMenu(
          this.$refs.leftMenu.$children,
          this.$route.path
        );
        if (matchedMenu) {
          this.$refs.leftMenu.select(this.$route.path);
        }

        // 显示页面顶部导航路径（面包屑）
        let data = this.$route.matched
          .filter(route => route.name)
          .map(route => {
            return { title: route.name };
          });
        if (data.length > 1) {
          this.breadcrumbDatas = data;
        } else {
          this.breadcrumbDatas = [];
        }
      }
    },
    /**
     * 检查路径是否跟菜单的key匹配，匹配的时候就要切换菜单，否则不切换。
     */
    matchedMenu(children, path) {
      let _this = this;
      for (let i = 0; i < children.length; i++) {
        let menu = children[i];
        if (path === menu.$options.propsData.data.key) {
          return true;
        }
        let next = _this.matchedMenu(menu.$children, path);
        if (next) {
          return true;
        }
      }
      return false;
    },
    trigger(data) {
      if (
        data.children &&
        data.children.length > 0 &&
        data.key.startsWith("/") &&
        data.key !== this.$route.path
      ) {
        // 如果有下级菜单，但是菜单的key本身是/开头的，那么就直接路由到页面，否则不路由
        this.$router.push(data.key);
      } else if (
        (!data.children || data.children.length === 0) &&
        data.key !== this.$route.path
      ) {
        // 没有下级菜单，也直接路由
        this.$router.push(data.key);
      }
    }
  },
  computed: {
    siderCollapsed: {
      get() {
        return this.$store.state.siderCollapsed;
      },
      set(value) {
        this.$store.commit("updateSiderCollapse", value);
      }
    }
  }
};
</script>

<style lang="less">
@import (less) "../css/fonts/style.less";
.h-layout-sider {
  width: 260px;
  min-width: 260px;
  max-width: 260px;
}
.sider-collapse {
  color: rgba(255, 255, 255, 0.65);
  background: #333333;
}

.layout-logo {
  height: 64px;
}
.layout-logo .logo {
  width: 50px;
  margin-top: 8px;
}
.layout-logo .title {
  display: inline-block;
  padding-top: 5px;
  font-size: 35px;
  vertical-align: top;
  color: rgba(255, 255, 255, 0.65);
}
.h-layout-sider {
  min-height: 100vh;
}
.h-layout-footer {
  padding: 24px 50px;
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
}
.title .router-link-exact-active,
.title .router-link-active {
  color: rgba(255, 255, 255, 0.65);
}

.h-layout-header {
  background-color: #f3f6f8;
}
</style>
