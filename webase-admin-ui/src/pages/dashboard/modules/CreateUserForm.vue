<template>
  <a-modal
    title="新增单号"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          label="用户名"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-select
            v-decorator="['username', {rules: [{required: true, message: '请输入至少两个字符的用户名称！'}]}]"
            show-search
            :value="uservalue"
            placeholder="输入可检索用户"
            :default-active-first-option="false"
            :show-arrow="false"
            :filter-option="false"
            :not-found-content="null"
            @blur="handleblur"
            @search="handleSearch"
            @change="handleChange">
            <a-select-option v-for="d in userdata" :key="d.username">
              {{ d.username }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="国内单号"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input v-decorator="['shipCd', {rules: [{required: true, message: '国内单号必须输入'}]}]"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { searchUser } from '@/api/manage'
import { createShipInfo } from '@/api/dashboard'

export default {
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 13 }
      },
      visible: false,
      confirmLoading: false,

      form: this.$form.createForm(this),
      roles: [],
      userdata: [],
      uservalue: ''
    }
  },
  created () {
  },
  methods: {
    add () {
      this.visible = true
    },
    handleSubmit () {
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
    },
    handleCancel () {
      this.visible = false
      this.form.resetFields()
    },
    handleChange (value) {
      this.uservalue = value
      console.log(`selected ${value}`)
    },
    handleblur () {
      if (this.userdata.length !== 0) {
        var user = this.userdata.filter(e => e.username === this.uservalue)
        if (user.length === 0) {
          alert('add user:' + this.uservalue)
        }
      }
    },
    handleSearch (value) {
      this.uservalue = value
      console.log(`selected ${value}`)
      searchUser({ userName: value }).then((res) => {
        console.log(res.result[0])
        this.userdata = res.result
      })
    }
  }
}
</script>
