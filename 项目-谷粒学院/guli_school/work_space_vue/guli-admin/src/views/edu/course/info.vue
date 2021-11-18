<template>
  <div class="app-container">
    <h2 style="text-align: center">发布新课程</h2>
    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-form label-width="120px">
      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写" />
      </el-form-item>

      <!-- 课程讲师 -->
      <el-form-item label="课程讲师">
        <el-select v-model="courseInfo.teacherId" placeholder="请选择">
          <el-option v-for="teacher in teacherList" :key="teacher.id" :label="teacher.name" :value="teacher.id" />
        </el-select>
      </el-form-item>
      <!-- 所属分类 TODO -->
      <el-form-item label="课程分类">
        <el-select v-model="courseInfo.subjectParentId" placeholder="一级分类" @change="changeData">
          <el-option v-for="item in oneSubjectList" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>

        <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
          <el-option v-for="item in twoSubJectList" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数" />
      </el-form-item>

      <!-- 课程简介 TODO -->
      <el-form-item label="课程简介">
        <tinymce :height="300" v-model="courseInfo.description" />
      </el-form-item>

      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload"
          :action="BASE_API + '/eduOss/file/avatar?host=cover'" class="avatar-uploader">
          <img :src="courseInfo.cover" width="200px" height="120px" />
        </el-upload>
      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元" />
        元
      </el-form-item>
      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import course from "@/api/edu/course.js";
  import subject from "@/api/edu/subject.js";
  import Tinymce from "@/components/Tinymce";

  export default {
    components: {
      Tinymce,
    },
    data() {
      return {
        saveBtnDisabled: false, // 保存按钮是否禁用
        courseInfo: {
          title: "",
          lessonNum: 0,
          description: "",
          price: 0,
          teacherId: "",
          subjectParentId: "",
          subjectId: "",
          cover: "/static/default_course_avoter.jpg", //课程封面
        },
        teacherList: [], //教师列表,
        oneSubjectList: [],
        twoSubJectList: [],
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
      console.log("info created");
      this.init();
    },
    methods: {

      init() {
        if (this.$route.params && this.$route.params.id) {
          //回显 修改
          course.getCourse(this.$route.params.id).then((response) => {
            this.courseInfo = response.data.data;

            //初始化分类列表,解决二级联动菜单回显异常
            subject.getTree().then((response) => {
              this.oneSubjectList = response.data.getTreeList;

              for (let index = 0; index < this.oneSubjectList.length; index++) {
                const element = this.oneSubjectList[index];
                if (element.id == this.courseInfo.subjectParentId) {
                  this.twoSubJectList = element.children;
                }
              }
            });
            // 获取讲师列表
            this.findAllTeacher();
          });
        } else {
          this.getOneSubject();
          this.findAllTeacher();
        }
      },
      changeData(selectId) {
        this.oneSubjectList.forEach((element) => {
          if (element.id == selectId) {
            this.twoSubJectList = element.children;
            this.courseInfo.subjectId = "";
          }
        });
      },
      //获取课程分类包含一级二级菜单
      getOneSubject() {
        subject
          .getTree()
          .then((result) => {
            this.oneSubjectList = result.data.getTreeList;
          })
          .catch((err) => {});
      },
      //查询所有讲师
      findAllTeacher() {
        course.getAll().then((response) => {
          this.teacherList = response.data.items;
        });
      },
      saveOrUpdate() {
        console.log("next");

        if (this.$route.params && this.$route.params.id) {
          course.update(this.courseInfo).then((response) => {
            //提示信息
            this.$message({
              type: "success",
              message: "修改课程信息成功",
            });
            //调整到第二步
            this.$router.push({
              path: `/course/chapter/${this.courseInfo.id}`,
            });
          });
        } else {
          course.addCourseInfo(this.courseInfo).then((response) => {
            //提示信息
            this.$message({
              type: "success",
              message: "添加课程信息成功",
            });
            //调整到第二步
            this.$router.push({
              path: `/course/chapter/${response.data.id}`,
            });
          });
        }
      },
      handleAvatarSuccess(res, file) {
        console.log(res); // 上传响应
        console.log(URL.createObjectURL(file.raw)); // base64编码
        this.courseInfo.cover = res.data.url;
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === "image/jpeg";
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error("上传头像图片只能是 JPG 格式!");
        }
        if (!isLt2M) {
          this.$message.error("上传头像图片大小不能超过 2MB!");
        }
        return isJPG && isLt2M;
      },


    },
  };

</script>
<style scoped>
  .tinymce-container {
    line-height: 29px;
  }

</style>
