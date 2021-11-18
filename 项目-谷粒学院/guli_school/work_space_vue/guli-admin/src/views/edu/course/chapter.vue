<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-button type="text" @click="openChapterDislog">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
      <li v-for="chapter in chapterList" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button type="text" @click="openVideoEdit(chapter.id)">添加课时</el-button>
            <el-button type="text" @click="updateChapter(chapter.id)">编辑</el-button>
            <el-button type="text" @click="deleteChapter(chapter.id)">删除</el-button>
          </span>
        </p>
        <!-- 视频 -->
        <ul class="chanpterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>{{ video.title }}
              <span class="acts">
                <el-button type="text" @click="updateVideo(video.id)">编辑</el-button>
                <el-button type="text" @click="deleteVideo(video.id)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>


    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0" controls-position="right" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title" />
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.isFree">
            <el-radio :label="1">免费</el-radio>
            <el-radio :label="0">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频">
          <!-- TODO -->
          <el-upload :on-success="handleVodUploadSuccess" :on-remove="handleVodRemove" :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed" :file-list="fileList" :action="BASE_API+'eduvod/video/upload'" :limit="1"
            class="upload-demo">
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">最大支持1G，<br>
                支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
  import chapter from "@/api/edu/chapter.js";
  import video from "@/api/edu/video.js";

  export default {
    data() {
      return {
        saveBtnDisabled: false, // 保存按钮是否禁用,
        chapterList: [],
        courseId: '',
        dialogChapterFormVisible: false, //添加章节弹框是否显示
        chapter: { //封装章节的数据
          title: "",
          sort: ""
        },
        dialogVideoFormVisible: false, //小节弹框是否显示
        saveVideoBtnDisabled: false, //小节保存按钮
        video: {
          title: "",
          sort: "",
          isFree: 0,
          videoSourceId: ""
        },
        fileList: [], //上传文件列表
        BASE_API: process.env.BASE_API // 接口API地址

      }
    },
    created() {
      console.log('chapter created')

      //获取路由的参数值
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
      }
      this.init();
    },
    methods: {
      init() {
        chapter.getAllChapterVideo(this.courseId).then((result) => {
          this.chapterList = result.data.data;
          console.log(result.data.data)
        })
      },
      /*
      上一步，跳转到info页面
      */
      previous() {
        if (this.$route.params && this.$route.params.id) {
          this.$router.push({
            path: '/course/info/' + this.$route.params.id
          })
        }
      },
      next() {
        console.log('next')
        this.$router.push({
          path: '/course/publish/' + this.courseId
        })
      },
      //------章节相关操作
      saveData() {
        //添加课程id
        this.chapter.courseId = this.courseId;

        chapter.addChapter(this.chapter).then(response => {
          //关闭弹框
          this.dialogChapterFormVisible = false;
          //提示信息
          this.$message({
            type: 'success',
            message: '保存成功!'
          });

          //刷新页面
          this.init();

        })
      },
      updateData() {
        chapter.updateChapter(this.chapter).then(response => {
          //关闭弹框
          this.dialogChapterFormVisible = false;
          //提示信息
          this.$message({
            type: 'success',
            message: '修改成功!'
          });

          //刷新页面
          this.init();
        })

      },
      saveOrUpdate() {

        if (this.chapter.id) {
          this.updateData()
        } else {
          this.saveData();
        }

      },
      /**
       * 修改章节弹框  回显
       */
      updateChapter(id) {
        this.dialogChapterFormVisible = true;
        chapter.queryChapter(id).then(response => {
          this.chapter = response.data.data;
        })
      },
      /**
       * 删除章节
       */
      deleteChapter(id) {

        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          chapter.deleteChapter(id).then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '修改成功!'
            });

            //刷新页面
            this.init();

          }).catch(erro => {
            //提示信息
            this.$message({
              type: 'error',
              message: erro.data.message
            });

          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });

      },
      openChapterDislog() {
        this.dialogChapterFormVisible = true;
        //清除数据
        this.chapter = {}
      },
      //-----------------小节相关操作
      openVideoEdit(chapterId) {
        this.video = {
          title: "",
          sort: "",
          isFree: 0,
          videoSourceId: "",
          videoOriginalName:""

        }; //清除之前数据
        this.fileList=[];
        this.video.chapterId = chapterId; //设置章节id
        this.video.courseId = this.courseId; //设置课程id
        this.dialogVideoFormVisible = true; //打开弹框

      },
      saveOrUpdateVideo() {
         
        if (this.video.id) { //更新数据

          video.updateVideoMethod(this.video).then((res) => {
            this.dialogVideoFormVisible = false; //关闭弹框

            //提示信息
            this.$message({
              type: 'success',
              message: '修改成功!'
            });

            //刷新页面
            this.init();

          });

        } else { //添加数据
          video.addVideo(this.video).then(response => {

            this.dialogVideoFormVisible = false; //关闭弹框

            //提示信息
            this.$message({
              type: 'success',
              message: '添加成功!'
            });

            //刷新页面
            this.init();

          });
        }

      },
      deleteVideo(id) {
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          video.deleteVideo(id).then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '删除成功!'
            });

            //刷新页面
            this.init();

          }).catch(erro => {
            //提示信息
            this.$message({
              type: 'error',
              message: erro.data.message
            });

          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      updateVideo(id) {

        video.getVideoById(id).then(response => {
          this.video = response.data.data; //回显数据
          this.dialogVideoFormVisible = true; //打开弹框
          this.fileList = [{
            'name': this.video.videoOriginalName
          }]
        })

      },
      //成功回调
      handleVodUploadSuccess(response, file, fileList) {
        this.video.videoSourceId = response.data.videoId
        this.video.videoOriginalName = file.name;
      },
      //视图上传多于一个视频
      handleUploadExceed(files, fileList) {
        this.$message.warning('想要重新上传视频，请先删除已上传的视频')
      },

      //删除前
      beforeVodRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`)
      },
      //删除
      handleVodRemove(file, fileList) {
        console.log(file)
        video.deleteAliyunVideo(this.video.videoSourceId).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          })
          //清空文件列表
          fileList = [];
          //删除后情况视频id
          this.video.videoSourceId = "";
          this.video.videoOriginalName = "";
        })
      },

    }
  }

</script>
<style scoped>
  .chanpterList {
    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;
  }

  .chanpterList li {
    position: relative;
  }

  .chanpterList p {
    float: left;
    font-size: 20px;
    margin: 10px 0;
    padding: 10px;
    height: 70px;
    line-height: 50px;
    width: 100%;
    border: 1px solid #DDD;
  }

  .chanpterList .acts {
    float: right;
    font-size: 14px;
  }

  .videoList {
    padding-left: 50px;
  }

  .videoList p {
    float: left;
    font-size: 14px;
    margin: 10px 0;
    padding: 10px;
    height: 50px;
    line-height: 30px;
    width: 100%;
    border: 1px dotted #DDD;
  }

</style>
