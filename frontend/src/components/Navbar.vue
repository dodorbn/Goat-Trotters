<script setup lang="ts">
</script>

<template>
  <nav class="navbar">

    <router-link to="/" class="brand-group">
      <img src="@/assets/logogoattrotters.png" alt="Logo Goat Trotters" class="logo">
      <h1 class="brand-text">GOAT TROTTERS</h1>
    </router-link>

    <ul class="nav-links">
      <li>
        <router-link to="/about" class="nav-item">L'Association</router-link>
      </li>
      <li>
        <router-link to="/questions" class="nav-item">Sondage</router-link>
      </li>
      <li>
        <router-link to="/stats" class="nav-item">Statistiques</router-link>
      </li>
    </ul>

    <div class="nav-actions">
      <router-link to="/contact" class="btn-contact">Nous écrire</router-link>
    </div>

  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.8rem 2rem;

  background-color: #120c24;
  border-bottom: 1px solid rgba(212, 175, 55, 0.2);

  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.4);
}

.brand-group {
  display: flex;
  align-items: center;
  gap: 1rem;
  text-decoration: none;
  transition: opacity 0.3s;
}

.brand-group:hover {
  opacity: 0.9;
}

.logo {
  height: 50px;
  width: auto;
  object-fit: contain;
}

.brand-text {
  color: white;
  font-size: 1.2rem;
  font-weight: 800;
  letter-spacing: 1px;
  text-transform: uppercase;
  margin: 0;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 2.5rem;
  margin: 0;
  padding: 0;
}

.nav-item {
  color: #ccc;
  text-decoration: none;
  font-weight: 500;
  font-size: 1rem;
  position: relative;
  transition: color 0.3s ease;
  padding: 5px 0;
}

.nav-item:hover {
  color: #D4AF37;
}

.router-link-active {
  color: #D4AF37;
  font-weight: bold;
}

.nav-item::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: 0;
  left: 0;
  background-color: #D4AF37;
  transition: width 0.3s ease;
}

.router-link-active::after {
  width: 100%;
}

.nav-item:hover::after {
  width: 100%;
}

.btn-contact {
  background: transparent;
  color: #D4AF37;
  border: 1px solid #D4AF37;
  padding: 8px 20px;
  border-radius: 50px;
  text-decoration: none;
  font-weight: bold;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.btn-contact:hover {
  background: #D4AF37;
  color: #120c24;
  transform: translateY(-2px);
  box-shadow: 0 0 15px rgba(212, 175, 55, 0.4);
}

@media (max-width: 768px) {
  .navbar {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
  }

  .nav-links {
    gap: 1.5rem;
    font-size: 0.9rem;
  }

  .brand-text {
    font-size: 1rem;
  }

  .nav-actions {
    display: none;
  }
}
</style>
