<template>
  <nav class="navbar">
    <h1>Goat Trotters 🐐</h1>
    <ul>
      <li><router-link to="/">Accueil</router-link></li>
      <li><router-link to="/about">L’association</router-link></li>
      <li><router-link to="/questions">Questions</router-link></li>
      <li><router-link to="/stats">Statistiques</router-link></li>
    </ul>
  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background: #ee4836;
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
