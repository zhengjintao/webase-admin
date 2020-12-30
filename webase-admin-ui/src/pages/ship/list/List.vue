<template>
  <div>
    <a-row :gutter="24">
      <a-col>
        <a-card
          class="project-list"
          :loading="loading"
          style="margin-bottom: 24px;"
          :bordered="false"
          title="待确认单号一览"
          :body-style="{ padding: 0 }">
          <div>
            <a-card-grid class="project-card-grid" :key="i" v-for="(item, i) in projectscnf">
              <a-card :bordered="false" :body-style="{ padding: 0 }">
                <a-card-meta>
                  <div slot="description" class="card-description">
                    单号；{{ item.title }}<br>
                  </div>
                </a-card-meta>
                <div class="project-item">
                  <a-checkbox :disabled="item.disabled" @change="onChange(item)" :itemid="item.id">
                    确定发货
                  </a-checkbox>
                  <p>{{ item.exp }}</p>
                </div>
              </a-card>
            </a-card-grid>
          </div>
        </a-card>
      </a-col>
      <a-col>
        <a-card
          class="project-list"
          :loading="loading"
          style="margin-bottom: 24px;"
          :bordered="false"
          title="我的货物一览"
          :body-style="{ padding: 0 }">
          <div>
            <a-card-grid class="project-card-grid" :key="i" v-for="(item, i) in projects">
              <a-card :bordered="false" :body-style="{ padding: 0 }">
                <a-card-meta>
                  <div slot="description" class="card-description">
                    国内单号；{{ item.title }}<br>
                    国际单号；{{ item.gshipCd }}<br>
                    状态；{{ item.description }}
                  </div>
                </a-card-meta>
              </a-card>
            </a-card-grid>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { timeFix } from '@/utils/util'
import { mapState } from 'vuex'

import { PageView } from '@/layouts'
import HeadInfo from '@/components/tools/HeadInfo'
import { Radar } from '@/components'

import { getUserListByUserName } from '@/api/dashboard'

export default {
  name: 'Workplace',
  components: {
    PageView,
    HeadInfo,
    Radar
  },
  data () {
    return {
      timeFix: timeFix(),
      avatar: '',
      user: {},
      checkboxStatus: [],
      projects: [],
      projectscnf: [],
      loading: true
    }
  },
  computed: {
    ...mapState({
      nickname: (state) => state.user.nickname,
      welcome: (state) => state.user.welcome
    }),
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    this.user = this.userInfo
  },
  mounted () {
    this.getProjects()
  },
  methods: {
    onChange (item) {
      if (item.checked) {
        var sec = 10
        var countdown = setInterval(function () {
          sec--
          item.second = sec
          item.exp = sec + '秒后将不能更改'
        }, 1000)
        item.countdown = countdown
        item.timeout = setTimeout(function () {
          item.disabled = true
          item.exp = ''
          clearInterval(countdown)
        }, sec * 1000)
      } else {
        if (item.countdown) {
          clearInterval(item.countdown)
        }

        if (item.timeout) {
          clearTimeout(item.timeout)
        }
        item.exp = ''
        item.disabled = false
      }

      item.checked = !item.checked
    },
    getProjects () {
      getUserListByUserName({ username: this.user.name }).then(res => {
        console.log('workplace -> call getRoleList()', res)
        res.result.forEach(element => {
          this.projects.push({
            id: element.id,
            disabled: false,
            checked: true,
            second: 0,
            exp: '',
            cover: '/logo.png',
            title: element.shipCd,
            description: '待确认发货',
            gshipCd: element.gshipCd,
            status: element.locked,
            updatedAt: '2018-07-26 00:00:00'
          })
        })

        this.projectscnf = this.projects.filter(element => element.status === 0)
        this.loading = false
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .project-list {

    .card-title {
      font-size: 0;

      a {
        color: rgba(0, 0, 0, 0.85);
        margin-left: 12px;
        line-height: 24px;
        height: 24px;
        display: inline-block;
        vertical-align: top;
        font-size: 14px;

        &:hover {
          color: #1890ff;
        }
      }
    }
    .card-description {
      color: rgba(0, 0, 0, 0.45);
      // height: 44px;
      line-height: 22px;
      overflow: hidden;
    }
    .project-item {
      display: flex;
      margin-top: 8px;
      overflow: hidden;
      font-size: 12px;
      height: 20px;
      line-height: 20px;
      a {
        color: rgba(0, 0, 0, 0.45);
        display: inline-block;
        flex: 1 1 0;

        &:hover {
          color: #1890ff;
        }
      }
      .datetime {
        color: rgba(0, 0, 0, 0.25);
        flex: 0 0 auto;
        float: right;
      }
    }
    .ant-card-meta-description {
      color: rgba(0, 0, 0, 0.45);
      height: 44px;
      line-height: 22px;
      overflow: hidden;
    }
  }

  .item-group {
    padding: 20px 0 8px 24px;
    font-size: 0;
    a {
      color: rgba(0, 0, 0, 0.65);
      display: inline-block;
      font-size: 14px;
      margin-bottom: 13px;
      width: 25%;
    }
  }

  .members {
    a {
      display: block;
      margin: 12px 0;
      line-height: 24px;
      height: 24px;
      .member {
        font-size: 14px;
        color: rgba(0, 0, 0, .65);
        line-height: 24px;
        max-width: 100px;
        vertical-align: top;
        margin-left: 12px;
        transition: all 0.3s;
        display: inline-block;
      }
      &:hover {
        span {
          color: #1890ff;
        }
      }
    }
  }

  .mobile {

    .project-list {

      .project-card-grid {
        width: 100%;
      }
    }

    .more-info {
      border: 0;
      padding-top: 16px;
      margin: 16px 0 16px;
    }

    .headerContent .title .welcome-text {
      display: none;
    }
  }

</style>
