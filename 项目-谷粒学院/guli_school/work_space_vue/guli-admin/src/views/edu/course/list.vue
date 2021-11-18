<template>
  <div class="app-container">
    <h1>课程列表</h1>

    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="课程名称">
        <el-input v-model="query.title" placeholder="课程名称" />
      </el-form-item>

      <el-form-item label="讲师">
        <el-select v-model="query.teacherId" clearable placeholder="姓名">
          <el-option v-for="item in teacherList" :value="item.id" :label="item.name" />
        </el-select>
      </el-form-item>

      <!-- 所属分类 TODO -->
      <el-form-item label="课程分类">
        <el-select v-model="query.subjectParentId" placeholder="一级分类" @change="changeData">
          <el-option v-for="item in oneSubjectList" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>

        <el-select v-model="query.subjectId" placeholder="二级分类">
          <el-option v-for="item in twoSubJectList" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList(1)">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 表格 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="数据加载中" border fit highlight-current-row
      row-class-name="myClassList">
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="课程信息" width="470" align="center">
        <template slot-scope="scope">
          <div class="info">
            <div class="pic">
              <img :src="scope.row.cover" alt="scope.row.title" width="150px">
            </div>
            <div class="title">
              <a href=""> {{ scope.row.title }} </a>
              <p>{{ scope.row.lessonNum }}课时</p>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtCreate.substr(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtModified.substr(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column label="价格" width="100" align="center">
        <template slot-scope="scope">
          {{ Number(scope.row.price) === 0 ? '免费' :
      '¥' + scope.row.price.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="buyCount" label="付费学员" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.buyCount }}人
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <router-link :to="'/course/info/'+scope.row.id">
            <el-button type="text" size="mini" icon="el-icon-edit">编辑课程信息</el-button>
          </router-link>
          <router-link :to="'/course/chapter/'+scope.row.id">
            <el-button type="text" size="mini" icon="el-icon-edit">编辑课程大纲</el-button>
          </router-link>
          <el-button type="text" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination :current-page="page" :page-size="limit" :total="total" style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper" @current-change="getList" />
</div>
</template>


<script>
  import course from "@/api/edu/course.js";
  import subject from "@/api/edu/subject.js";

  export default {
    //写核心代码位置
    data() {
      //定义变量和初始值
      return {
        list: null, //返回的行数据
        page: 1,
        limit: 5,
        total: 0,
        listLoading:false,
        query: { //查询条件
          subjectId: "",
          subjectParentId: "",
          teacherId: "",
          title: ""
        },
        teacherList: [], // 讲师列表
        oneSubjectList: [], // 一级分类列表
        twoSubJectList: [] // 二级分类列表,
      };
    },

    created() {
      //页面渲染之前执行，调用方法
      this.findAllTeacher(); //获取讲师列表
      this.getOneSubject(); //获取课程分类

      this.getList(); //获取数据
    },
    methods: {
      //获取课程分类包含一级二级菜单
      getOneSubject() {
        subject
          .getTree()
          .then((result) => {
            this.oneSubjectList = result.data.getTreeList;
          })
          .catch((err) => {});
      },
      changeData(selectId) {
        this.oneSubjectList.forEach((element) => {
          if (element.id == selectId) {
            this.twoSubJectList = element.children;
            this.courseInfo.subjectId = "";
          }
        });
      },
      //查询所有讲师
      findAllTeacher() {
        course.getAll().then((response) => {
          this.teacherList = response.data.items;
        });
      },

      //根据条件获取分页列表
      getList(pageNo = 1) {
        this.page = pageNo;
        debugger
        course.getCoursePage(this.page, this.limit, this.query).then((response) => {
          console.log(response);
          this.list = response.data.rows;
          this.total = response.data.total;
        });

      },
      //情况查询数据，并分页查询所有页面
      resetData() {
        this.query = {};
        this.getList();
      },
      //根据id删除
      removeDataById(id) {
        this.$confirm("此操作将永久删除该课程, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          course.deleteCourse(id).then((respone) => {
            //提示信息
            this.$message({
              type: "success",
              message: "删除成功!",
            });

            //回到列表页面
            this.getList();
          });
        });
      },
    },
  };

</script>
<style scoped>
  .myClassList .info {
    width: 450px;
    overflow: hidden;
  }

  .myClassList .info .pic {
    width: 150px;
    height: 90px;
    overflow: hidden;
    float: left;
  }

  .myClassList .info .pic a {
    display: block;
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
  }

  .myClassList .info .pic img {
    display: block;
    width: 100%;
  }

  .myClassList td .info .title {
    width: 280px;
    float: right;
    height: 90px;
  }

  .myClassList td .info .title a {
    display: block;
    height: 48px;
    line-height: 24px;
    overflow: hidden;
    color: #00baf2;
    margin-bottom: 12px;
  }

  .myClassList td .info .title p {
    line-height: 20px;
    margin-top: 5px;
    color: #818181;
  }

</style>
