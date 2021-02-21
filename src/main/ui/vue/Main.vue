<template>
  <div>
<!--    <nav-bar/>-->
    <file-loader/>
  </div>
</template>

<script>
import NavBar from './NavBar';
import FileLoader from './FileLoader';

export default {
  name: 'Main',
  components: {FileLoader, NavBar},
};
</script>

<style scoped>

</style>