<script lang="ts"></script>

<template>
  <el-header class="header">
    <!--        <RouterLink to="/">Home</RouterLink>-->
    <!--        <RouterLink to="/write">글 작성</RouterLink>-->
    <el-menu
        mode="horizontal"
        router
    >
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">Write</el-menu-item>
    </el-menu>
  </el-header>
</template>

<style scoped>
header {
  margin: 0;
}
.header {
  padding: 0;
  height: 60px;
}
</style>