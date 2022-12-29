<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");

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

axios.get(`/api/posts/${props.postId}`)
    .then(response => {
      post.value = response.data;
    });

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({name: "home"})
  })
}
</script>

<template>
  <div>
    <el-input v-model="post.title" type="text" placeholder="제목을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">글 수정 완료</el-button>
  </div>
</template>

<style>

</style>