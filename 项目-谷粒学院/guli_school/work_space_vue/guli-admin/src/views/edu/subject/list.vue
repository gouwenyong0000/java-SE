<template>
  <div class="app-container">
    <el-input
      v-model="filterText"
      placeholder="Filter keyword"
      style="margin-bottom: 30px"
    />

    <el-tree
      ref="tree2"
      :data="data2"
      :props="defaultProps"
      :filter-node-method="filterNode"
      class="filter-tree"
      default-expand-all
    />
  </div>
</template>

<script>
import subject from "@/api/edu/subject.js";
export default {
  data() {
    return {
      filterText: "",
      data2: [], //返回分类数据
      defaultProps: {
        children: "children", //属性分类
        label: "label", // 显示标题
      },
    };
  },
  created() {
    this.getData();
  },
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val);
    },
  },

  methods: {
    getData() {
      subject.getTree().then((response) => {
        this.data2 = response.data.getTreeList;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    },
  },
};
</script>

