<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name" />
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" />
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师" />
          <el-option :value="2" label="首席讲师" />
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career" />
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea" />
      </el-form-item>

      <!-- 讲师头像 -->
      <el-form-item label="讲师头像">
        <!-- 头衔缩略图 -->
        <pan-thumb :image="teacher.avatar" />
        <!-- 文件上传按钮 -->
        <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow = true">更换头像
        </el-button>
        <!--
            v-show：是否显示上传组件
            :key：类似于id，如果一个页面多个图片上传控件，可以做区分
            :url：后台上传的url地址
            @close：关闭上传组件
            @crop-upload-success：上传成功后的回调 -->
        <image-cropper v-show="imagecropperShow" :width="300" :height="300" :key="imagecropperKey"
          :url="BASE_API + '/eduOss/file/avatar'" field="file" @close="close" @crop-upload-success="cropSuccess" />
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<!-- js-->
<script>
  import teacherApi from "@/api/edu/teacher.js";
  import ImageCropper from "@/components/ImageCropper";
  import PanThumb from "@/components/PanThumb";

  export default {
    components: {
      ImageCropper,
      PanThumb
    },
    data() {
      return {
        teacher: {
          name: "",
          sort: 0,
          level: 1,
          career: "",
          intro: "",
          avatar: "",
        },
        saveBtnDisabled: false, // 保存按钮是否禁用,
        imagecropperShow: false, //上传弹框组件是否显示
        imagecropperKey: 0, //上传组件key值
        BASE_API: process.env.BASE_API, //获取dev.env.js里面的BASE_API值
      };
    },
    watch: {
      $route(to, from) {
        console.log("watch $route");
        this.init();
      },
    },
    created() {
      console.log("created");
      this.init();
    },

    methods: {
      init() {
        if (this.$route.params && this.$route.params.id) {
          const id = this.$route.params.id;
          this.fetchDataById(id);
        } else {
          this.teacher = {};
        }
      },
      saveOrUpdate() {
        this.saveBtnDisabled = true;
        if (!this.teacher.id) {
          this.saveData();
        } else {
          this.updateTeacher();
        }
      },
      // 保存
      saveData() {
        teacherApi
          .saveTeacher(this.teacher)
          .then((responese) => {
            //提示信息
            this.$message({
              type: "success",
              message: "添加成功!",
            });
            //回到列表页面 路由调整
            this.$router.push({
              path: "/teacher/table"
            });
          })
          .catch((erro) => {});
      }, // 更新
      updateTeacher() {
        teacherApi
          .updateTeacher(this.teacher)
          .then((responese) => {
            //提示信息
            this.$message({
              type: "success",
              message: "修改成功!",
            });
            //回到列表页面 路由调整
            this.$router.push({
              path: "/teacher/table"
            });
          })
          .catch((erro) => {});
      },
      // 根据id查询记录
      fetchDataById(id) {
        teacherApi
          .getTeacherInfo(id)
          .then((response) => {
            console.log(response);
            this.teacher = response.data.teacher;
          })
          .catch((response) => {
            this.$message({
              type: "error",
              message: "获取数据失败",
            });
          });
      },
      //关闭上传图片弹框
      close() {
        this.imagecropperShow = false;

        //上传组件初始化
        this.imagecropperKey = this.imagecropperKey + 1;
      },
      //图片上传成功
      cropSuccess(data) {
        this.teacher.avatar = data.url;

        //关闭上传图片弹框
        this.imagecropperShow = false;

        //上传组件初始化
        this.imagecropperKey = this.imagecropperKey + 1;
      },
    },
  };

</script>
