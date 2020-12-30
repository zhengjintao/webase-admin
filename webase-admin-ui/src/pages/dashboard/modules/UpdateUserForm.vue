<template>
  <a-modal
    title="编辑用户"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          label="用户编号"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input disabled="disabled" v-decorator="['id']"/>
        </a-form-item>
        <a-form-item
          label="国内单号"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input v-decorator="['shipCd', {rules: [{required: true, min: 2, message: '请输入至少两个字符的用户名称！'}]}]"/>
        </a-form-item>
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
            @search="handleSearch"
            @change="handleChange">
            <a-select-option v-for="d in userdata" :key="d.id">
              {{ d.username }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="国际单号"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input v-decorator="['gshipCd']"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { searchUser } from '@/api/manage'
import { updateShipInfo } from '@/api/dashboard'
import pick from 'lodash.pick'

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
    edit (record) {
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(record, 'id', 'username', 'shipCd', 'gshipCd'))
      })
    },
    handleSubmit () {
      const { form: { validateFields } } = this
      this.confirmLoading = true
      validateFields((errors, values) => {
        if (!errors) {
          updateShipInfo({
            id: values.id,
            username: values.username,
            shipCd: values.shipCd,
            gshipCd: values.gshipCd
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
