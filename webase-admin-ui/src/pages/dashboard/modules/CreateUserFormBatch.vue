<template>
  <a-modal
    title="单号批量新增"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-tabs v-model="activetabkey" @tabClick="tabClickHandler">
        <a-tab-pane key="1" tab="填写单号">
          <div>格式:【行号/单号/姓名/重量】以制表符号隔开</div>
          <a-form :form="form">
            <a-textarea rows="15" v-model="content" placeholder="1	1234567890	张三	1.5kg" v-decorator="['shipCd', {rules: [{required: true, message: '国内单号必须输入'}]}]"/>
          </a-form>
        </a-tab-pane>
        <a-tab-pane key="2" tab="一览">
          <a-table
            :columns="tablecolumns"
            :data-source="tabledata"
            :pagination="{ pageSize: 5 }"
          >
            <template #footer="">
              <a-checkbox v-model="chkstatus" @change="chkchanged">
                只显示错误行(错误行数{{ errcnt }}/总行数{{ totalcnt }})
              </a-checkbox>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>
import { searchUser } from '@/api/manage'
import { createShipInfo } from '@/api/dashboard'

export default {
  data () {
    return {
      visible: false,
      confirmLoading: false,
      chkstatus: false,
      form: this.$form.createForm(this),
      btnsubmit: false,
      activetabkey: '1',
      roles: [],
      userdata: [],
      uservalue: '',
      content: '',
      errcnt: 0,
      totalcnt: 0,
      tablecolumns: [
        {
          title: '行号',
          dataIndex: 'key',
          slots: { customRender: 'name' }
        },
        {
          title: '单号',
          className: 'column-money',
          dataIndex: 'money'
        },
        {
          title: '姓名',
          dataIndex: 'address'
        },
        {
          title: '重量',
          dataIndex: 'name'
        },
        {
          title: '',
          dataIndex: 'status'
        }
      ],
      tabledata: [],
      tabledatabak: []
    }
  },
  created () {
  },
  methods: {
    tabClickHandler (params) {
      this.activetabkey = params
      if (params === '2') {
        this.formatDataLine()
      }
    },
    add () {
      this.visible = true
    },
    chkchanged () {
      var errdata = this.tabledatabak.filter(e => e.status === '❌')
      this.tabledata = this.chkstatus ? errdata : this.tabledatabak
      this.errcnt = errdata.length
      this.totalcnt = this.tabledatabak.length
    },
    formatDataLine () {
      var arrs = this.content.split('\n')
      this.tabledatabak = []
      arrs.forEach(e => {
        if (e.length === 0) {
          return
        }
        var items = e.split('\t')
        this.tabledatabak.push({
          key: items.length > 0 ? items[0] : '',
          money: items.length > 1 ? items[1] : '',
          address: items.length > 2 ? items[2] : '',
          name: items.length > 3 ? items[3] : '',
          status: items.length < 3 ? '❌' : '🟢'
        })
      })
      this.chkchanged()
    },
    handleSubmit () {
      if (!this.btnsubmit) {
        this.activetabkey = '2'
        this.formatDataLine()
      } else {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            createShipInfo({
              username: values.username,
              shipCd: values.shipCd
            }).then((res) => {
              this.confirmLoading = false
              if (res.success) {
                this.visible = false
                this.form.resetFields()
                this.$emit('ok', values)
              } else {
                this.$message.warning(res.message)
              }
            })
          } else {
            this.confirmLoading = false
          }
        })
      }
    },
    handleCancel () {
      this.visible = false
      this.form.resetFields()
    },
    handleChange (value) {
      this.uservalue = value
      console.log(`selected ${value}`)
    },
    handleSearch (value) {
      this.uservalue = undefined
      console.log(`selected ${value}`)
      searchUser({ userName: value }).then((res) => {
        console.log(res.result[0])
        this.userdata = res.result
      })
    }
  }
}
</script>
