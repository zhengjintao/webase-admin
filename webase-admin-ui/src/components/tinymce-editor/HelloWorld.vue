<template>
  <div>
    <tinymce-editor
      ref="editor"
      v-model="msg"
      :disabled="disabled"
      :base-url="baseUrl"
      :language="language"
      :skin="skin"
      @onClick="onClick">
    </tinymce-editor>
  </div>
</template>

<script>
import TinymceEditor from './tinymce-editor'
export default {
  components: {
    TinymceEditor
  },
  data () {
    return {
      disabled: false,
      baseUrl: process.env.NODE_ENV === 'production' ? '/' : '',
      language: 'zh_CN',
      skin: 'oxide'
    }
  },
  methods: {
    // 鼠标单击的事件
    onClick (e, editor) {
      console.log('Element clicked')
      console.log(e)
      console.log(editor)
    },
    // 清空内容
    clear () {
      this.$refs.editor.clear()
    }
  }
}
</script>
