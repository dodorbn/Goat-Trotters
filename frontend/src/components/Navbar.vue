<template>
  <nav class="navbar">
    <router-link to="/" class="brand">
      <img src="@/assets/logogoattrotters.png" alt="Logo Goat Trotters" class="logo">
      <h1>Goat Trotters</h1>
    </router-link>
    <ul class="nav-links">
      <li><router-link to="/about">L'association</router-link></li>
      <li><router-link to="/questions">Questions</router-link></li>
      <li><router-link to="/stats">Statistiques</router-link></li>
    </ul>

    <!-- Élément vide pour maintenir l'équilibre dans le flex -->
    <div class="spacer"></div>

  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background: #000000;
}

.brand {
  display: flex;
  align-items: center;
  gap: 1rem;
  text-decoration: none;
  color: white;
  flex: 1;
}

.logo {
  height: 80px;
  width: auto;
}

ul {
  list-style: none;
  display: flex;
  gap: 1rem;
}
a {
  color: white;
  text-decoration: none;
  font-weight: bold;
}
a.router-link-exact-active {
  text-decoration: underline;
}
</style>
<script setup lang="ts">
</script>
