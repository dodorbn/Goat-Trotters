<template>
  <div class="home-container">
    <!-- Hero Section -->
    <header class="hero-section">
      <h1>Bienvenue chez les Goat Trotters 🐐</h1>
      <p class="hero-subtitle">Explorez l'Europe à travers nos yeux et nos rencontres</p>
    </header>

    <!-- Carte Interactive -->
    <section class="section map-section">
      <h2>Notre Voyage en Direct</h2>
      <p class="section-description">Suivez notre progression à travers l'Europe</p>
      <div class="map-container">
        <MapView />
      </div>
    </section>

    <!-- Dernières Aventures -->
    <section class="section">
      <h2>Nos Dernières Aventures</h2>
      <div class="adventures-grid">
        <div class="adventure-card">
          <h3>Podcasts</h3>
          <p>Écoutez nos dernières interviews et découvertes culturelles à travers l'Europe.</p>
          <a href="https://open.spotify.com/show/goattrotters"
             target="_blank"
             rel="noopener noreferrer"
             class="action-link">Écouter nos podcasts</a>
        </div>
        <div class="adventure-card">
          <h3>Vlogs</h3>
          <p>Vivez nos aventures en images et partagez nos moments les plus marquants.</p>
          <a href="https://youtube.com/@goattrotters"
             target="_blank"
             rel="noopener noreferrer"
             class="action-link">Regarder nos vlogs</a>
        </div>
        <div class="adventure-card">
          <h3>Application</h3>
          <p>Téléchargez notre application pour être au plus près de notre voyage.</p>
          <a href="https://play.google.com/store/apps/details?id=com.airgoat.goat&utm_source=emea_Med"
             target="_blank"
             rel="noopener noreferrer"
             class="action-link">Télécharger l'application</a>
        </div>
      </div>
    </section>

    <!-- Call to Action -->
    <section class="section cta-section">
      <h2>Participez à l'Aventure !</h2>
      <p>Rejoignez notre communauté et partagez vos propres expériences de voyage.</p>
      <div class="cta-buttons">
        <router-link to="/about" class="cta-button">Découvrir l'Association</router-link>
        <a href="mailto:goat.trotters@mail.com" class="cta-button secondary">Nous Contacter</a>
      </div>
    </section>
  </div>
</template>

<script setup>
import MapView from '../components/MapView.vue'
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hero-section {
  text-align: center;
  margin-bottom: 3rem;
  padding: 4rem 0;
  background-size: cover;
  border-radius: 8px;
  color: white;
}

.hero-section h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  color: #D4AF37;
}

.hero-subtitle {
  font-size: 1.5rem;
  opacity: 0.9;
}

.section {
  margin-bottom: 4rem;
}

.section h2 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #2c3e50;
  border-bottom: 2px solid #D4AF37;
  padding-bottom: 0.5rem;
  display: inline-block;
}

.section-description {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
}

.map-container {
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.adventures-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.adventure-card {
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.adventure-card:hover {
  transform: translateY(-5px);
}

.adventure-card h3 {
  color: #D4AF37;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.action-link {
  display: inline-block;
  margin-top: 1rem;
  color: #D4AF37;
  text-decoration: none;
  font-weight: bold;
}

.action-link:hover {
  text-decoration: underline;
}

.cta-section {
  text-align: center;
  background-color: #f8f8f8;
  padding: 3rem;
  border-radius: 8px;
}

.cta-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}

.cta-button {
  padding: 1rem 2rem;
  background-color: #D4AF37;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-weight: bold;
  transition: background-color 0.3s;
}

.cta-button.secondary {
  background-color: transparent;
  border: 2px solid #D4AF37;
  color: #D4AF37;
}

.cta-button:hover {
  background-color: #B8952D;
}

.cta-button.secondary:hover {
  background-color: #D4AF37;
  color: white;
}

@media (max-width: 768px) {
  .hero-section {
    padding: 3rem 1rem;
  }

  .hero-section h1 {
    font-size: 2rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .adventures-grid {
    grid-template-columns: 1fr;
  }

  .cta-buttons {
    flex-direction: column;
  }
}
</style>
