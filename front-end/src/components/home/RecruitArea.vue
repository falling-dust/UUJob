<template>
  <div class="box-card-up">
    <h4 class="heading h3 text-white pt-3">求职招聘栏</h4>
    <el-menu :default-active="'0'" class="el-menu rounded" mode="horizontal" style="margin-bottom: 10px;"
      @select="handleSelect">
      <el-menu-item index="0">全部</el-menu-item>
      <el-menu-item index="1">岗位</el-menu-item>
      <el-menu-item index="2">企业</el-menu-item>
      <el-menu-item index="3">喜好</el-menu-item>
    </el-menu>

    <div v-for="recruitObj in paginatedRecruits">
      <el-card class="box-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span style="font-size:larger">{{ recruitObj.title }}</span>

            <el-button class="button" color="black" @click="enterJob(recruitObj.id)"> <el-icon><MagicStick /></el-icon>详情</el-button>
          </div>
          <span style="font-size: 8px;color: gray;">发布时间:{{ recruitObj.date }}</span>
        </template>
        <div class="row">
          <div class="col-8" style="display:flex">
            <div class="position">
              {{ recruitObj.position }}
            </div>
            <div class="ml-3" :style="{ color: getStatusColor(recruitObj.status), marginTop: '5px' }">
              {{ getStatusText(recruitObj.status) }}
            </div>
          </div>
          <div class="col-4">
            <div class="salary">
              {{ recruitObj.salary }}
            </div>
          </div>
        </div>
        <div class="description">
          {{ recruitObj.description.slice(0, 50) }}
        </div>

      </el-card>
    </div>

    <div>
      <el-pagination :hide-on-single-page="totalRecruitObjs <= 4 ? true : false" @current-change="handlePageChange"
        :current-page="currentPage" :page-size="pageSize" :total="totalRecruitObjs" layout="prev, pager, next" background>
      </el-pagination>
    </div>


  </div>
</template>
  
<script>
import { ref, watch } from 'vue';
import axios from "axios";
import Apply from "@/components/home/Apply.vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default {
  components: {
    Apply,
  },
  setup() {
    const router = useRouter();

    return {
      router
    }

  },
  data() {

    return {
      router: useRouter(),
      userId: useStore().state.userId,
      select_id: "",
      dialogApplyVisible: false,
      pagedRecruitObjs: [], // 当前页展示的求职帖子的数组
      currentPage: 1, // 当前页码
      pageSize: 4, // 每页显示的数量
      totalRecruitObjs: 0, // 总的求职帖子数量
      filteredRecruits: [],

      recruitObjs: [
        {
          id: "1",
          title: "蔚蓝求职",
          position: "算法工程师",
          salary: "1.5w-2w",
          description: "快来家人们",
          date: ''
        },
        {
          id: "2",
          title: "蔚蓝求职",
          position: "算法工程师",
          salary: "1.5w-2w",
          description: "快来家人们",
          date: ''
        },

      ]
    }
  },
  methods: {
    handlePageChange(currentPage) {
      this.currentPage = currentPage;
    },

    enterJob(id) {
      axios({
        method: "put",
        url: "http://localhost:9090/jobs/hits",
        data: {
          userId: this.userId,
          jobId: id,
        }
      }).then(function (response) {
        if (response.data.code == 200) {
          console.log("hit");
        } else {
          alert("error");
        }
      })
      this.router.push("../job/" + id)
    },

    handleChange(val) {
      console.log(val)
    },

    async getRecruitList() {
      let that = this;
      axios({
        method: "get",
        url: "http://localhost:9090/jobs/basis",
      }).then(function (response) {
        if (response.data.code == 200) {
          that.recruitObjs = response.data.data
          for (var item of that.recruitObjs) {
            var tmp = new Date(item.date)
            item.date = tmp.getFullYear() + "-" + (tmp.getMonth() + 1).toString().padStart(2, '0') + "-" + tmp.getDate().toString().padStart(2, '0');
          }
          //以前是怎么写出这种nt东西的
          // that.recruitObjs = [];
          // response.data.data.forEach(element => {
          //   that.recruitObjs.push(
          //     {
          //       id: element.id,
          //       title: element.title,
          //       position: element.position,
          //       salary: element.salary,
          //       description: element.description,
          //     }
          //   )
          // });

          that.filteredRecruits = that.recruitObjs;
        } else {
          alert("error");
        }
      })

    },

    handlePageChange(currentPage) {
      this.currentPage = currentPage;
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },

    async searchJobs(searchKeyword) {
      if (!searchKeyword) {
        this.filteredRecruits = this.recruitObjs;
        return;
      }
      this.filteredRecruits = this.recruitObjs.filter(
        (recruit) =>
          recruit.title.toLowerCase().includes(searchKeyword.toLowerCase()) ||
          recruit.position.toLowerCase().includes(searchKeyword.toLowerCase()) ||
          recruit.description.toLowerCase().includes(searchKeyword.toLowerCase())
      );
    },

    getStatusText(status) {
      if (status === 1) {
        return "招聘中";
      } else if (status === 2) {
        return "已终止";
      } else {
        return "";
      }
    },
    // 根据状态值返回对应的文字颜色样式
    getStatusColor(status) {
      if (status === 1) {
        return "rgb(107, 171, 240)";
      } else if (status === 2) {
        return "red";
      } else {
        return "";
      }
    },
  },
  computed: {
    paginatedRecruits() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.filteredRecruits.slice(startIndex, endIndex);
    },
  },
  mounted() {
    this.getRecruitList();
  },
  watch: {
    // searchKeyword: {
    //   immediate: true,
    //   handler() {
    //     this.searchJobs();
    //   },
    // },
    filteredRecruits: {
      immediate: true,
      handler() {
        //this.filteredRecruits = this.recruitObjs;
        this.totalRecruitObjs = this.filteredRecruits.length;
      },
    },
  },
}

</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0px;
  border-bottom: 0px;
  padding: 0px;
  width: 100%;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 100%;
  margin-bottom: 10px;
}

.box-card-up {
  width: 100%;
  height: 100%;
  overflow: hidden;
  min-height: 800px;
}

.position {
  font-size: large;
  margin-bottom: 10px;
}

.salary {
  font-size: medium;
  float: right;
  color: rgb(107, 171, 240);
}

.description {
  font-size: small;
  color: rgb(137, 137, 137);
}
</style>
  