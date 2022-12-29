<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const posts = ref([]);

axios.get("/api/posts?page=1&size=5")
    .then(response => {
      response.data.forEach((r: any) => {
        posts.value.push(r);
      });
    });

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{ name: 'read', params: {postId: post.id} }">{{ post.title }}</router-link>
      </div>

      <div class="content_body">{{ post.content }}</div>

      <div class="d-flex sub">
        <div class="category">개발</div>
        <div class="regDate">2022-12-29</div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
        font-size: 1.2rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content_body {
      font-size: 0.85rem;
      color: #7e7e7e;
      margin-top: 8px;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 8px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}

</style>
