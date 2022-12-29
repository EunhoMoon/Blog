<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  }
});

const post = ref({
  id: 0,
  title: "",
  content: ""
});

const moveToEdit = () => {
  router.push({name: "edit", params: {postId: props.postId}})
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`)
      .then(response => {
        post.value = response.data;
      });
})
</script>

<template>
  <el-row>
    <el-col>
      <h2 class="read_title">{{ post.title }}</h2>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2022-12-29 17:45:53</div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="read_content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.read_title {
  font-size: 1.6rem;
  font-weight: bold;
  color: #383838;
}

.read_content {
  font-size: 0.95rem;
  margin-top: 20px;
  color: #6b6b6b;
  white-space: break-spaces;
  line-height: 1.5;
}

.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
</style>