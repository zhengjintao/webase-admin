<template>
  <div class="tinymce-editor">
    <editor
      v-model="myValue"
      :init="init"
      :disabled="disabled"
      @onClick="onClick">
    </editor>
  </div>
</template>
<script>
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver'
import 'tinymce/icons/default'
// 编辑器插件plugins
// 更多插件参考：https://www.tiny.cloud/docs/plugins/
import 'tinymce/plugins/image'// 插入上传图片插件
import 'tinymce/plugins/media'// 插入视频插件
import 'tinymce/plugins/table'// 插入表格插件
import 'tinymce/plugins/lists'// 列表插件
import 'tinymce/plugins/wordcount'// 字数统计插件
import 'tinymce/plugins/fullscreen'
import 'tinymce/plugins/emoticons'
import 'tinymce/plugins/preview'
import 'tinymce/plugins/link'
import 'tinymce/plugins/print'
import 'tinymce/plugins/hr'
import 'tinymce/plugins/contextmenu'
import 'tinymce/plugins/imagetools'
import 'tinymce/plugins/searchreplace'
import 'tinymce/plugins/code'
import 'tinymce/plugins/emoticons/js/emojis'

import { axios } from '@/utils/request'
export default {
  components: {
    Editor
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    // 基本路径，默认为空根目录，如果你的项目发布后的地址为目录形式，
    // 即abc.com/tinymce，baseUrl需要配置成tinymce，不然发布后资源会找不到
    baseUrl: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plugins: {
      type: [String, Array],
      default: 'lists image media table wordcount emoticons fullscreen preview link print hr imagetools searchreplace code'
    },
    toolbar: {
      type: [String, Array],
      default: 'undo redo searchreplace |  bold italic underline strikethrough blockquote hr | alignleft aligncenter alignright alignjustify |  forecolor backcolor | fontselect fontsizeselect formatselect | bullist numlist outdent indent | lists emoticons link image media table | removeformat fullscreen preview print code'
    }
  },
  data () {
    return {
      init: {
        language_url: `${this.baseUrl}/tinymce/langs/zh_CN.js`,
        language: 'zh_CN',
        skin_url: `${this.baseUrl}/tinymce/skins/ui/oxide`, // 暗色系
        height: 300,
        plugins: this.plugins,
        toolbar: this.toolbar,
        menubar: false,
        block_formats: 'Paragraph=p;Header 1=h1;Header 2=h2;Header 3=h3',
        formats: {
          // A custom format that wraps blocks into a div with the specified wrapper class
          'custom-wrapper': { block: 'blockquote', classes: 'wrapper', wrapper: true }
        },
        contextmenu: 'link image media',
        emoticons_database: 'emojis',
        // 此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
        // 如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
        images_upload_handler: this.image_upload_handler,
        content_style: `
          blockquote {
            border-left: 4px solid gray;
            padding: 0px 0px 0px 2px;
            margin: 1px 0;
            font-size: 100%;
          }
            `
      },
      myValue: this.value
    }
  },
  mounted () {
    tinymce.init({})
  },
  methods: {
    image_upload_handler (blobInfo, success, failure, progress) {
      var formData = new FormData()
      formData.append('file', blobInfo.blob(), blobInfo.filename())
      axios.post(
        '/single-file',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      ).then(function () {
        success()
      })
        .catch(function () {
          failure('Image upload failed due to a XHR Transport error.')
        })
    },
    // 添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
    // 需要什么事件可以自己增加
    onClick (e) {
      this.$emit('onClick', e, tinymce)
    },
    // 可以添加一些自己的自定义事件，如清空内容
    clear () {
      this.myValue = ''
    }
  },
  watch: {
    value (newValue) {
      this.myValue = newValue
    },
    myValue (newValue) {
      this.$emit('input', newValue)
    }
  }
}

</script>
