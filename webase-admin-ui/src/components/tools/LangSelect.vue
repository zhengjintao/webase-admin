<template>
  <a-dropdown>
    <span class="action global-lang">
      <a-icon type="global" style="font-size: 16px"/>
    </span>
    <a-menu slot="overlay" style="width: 150px;" @click="SwitchLang">
      <a-menu-item v-for=" lang in langList" :key="lang.key">
        {{ lang.key.toLowerCase() + ' ' + lang.name }}
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>
// import { mixin as langMixin } from '@/store/i18n-mixin'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'LangSelect',
  // mixins: [langMixin],
  data () {
    return {
      langList: [
        { key: 'CN', name: '简体中文', alias: '简体' },
        { key: 'HK', name: '繁體中文', alias: '繁體' },
        { key: 'US', name: 'English', alias: 'English' }
      ]
    }
  },
  computed: {
    ...mapState({
      // 动态主路由
      lang: state => state.user.lang
    }),
    langAlias () {
      const lang = this.langList.find(item => item.key === this.lang)
      return lang.alias
    }
  },
  methods: {
    ...mapActions(['SetLang']),
    SwitchLang (row) {
      this.SetLang(row.key)
    }
  }
}
</script>
