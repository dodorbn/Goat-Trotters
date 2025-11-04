<template>
  <div class="about-container">
    <!-- En-tête de la page -->
    <header class="about-header">
      <h1>À propos de Goat Trotters</h1>
      <p class="subtitle">Découvrez notre association et nos valeurs</p>
    </header>

    <!-- Section : Qui sommes-nous ? -->
    <section class="section">
      <h2>Qui sommes-nous ?</h2>
      <p>
        <strong>Goat Trotters</strong> est une association née de la passion de trois amis pour les voyages, les rencontres interculturelles et le partage d'expériences authentiques.
        À travers nos <strong>road trips en Europe</strong>, nous documentons nos aventures sous forme de podcasts et de vlogs, pour sensibiliser la jeunesse aux richesses culturelles de notre continent.
      </p>
    </section>

    <!-- Section : Nos objectifs -->
    <section class="section">
      <h2>Nos objectifs</h2>
      <div class="objectives">
        <div class="objective-card">
          <h3>Sensibiliser</h3>
          <p>Mettre en lumière la diversité culturelle européenne à travers des récits et des rencontres.</p>
        </div>
        <div class="objective-card">
          <h3>Inspirer</h3>
          <p>Encourager les jeunes à voyager, découvrir et s'ouvrir aux autres.</p>
        </div>
        <div class="objective-card">
          <h3>Partager</h3>
          <p>Diffuser nos aventures via des podcasts, des vlogs et des réseaux sociaux.</p>
        </div>
      </div>
    </section>

    <!-- Section : Nos fondateurs -->
    <section class="section">
      <h2>Nos fondateurs</h2>
      <div class="founders">
        <div class="founder-card">
          <img src="../assets/photo%20de%20profil/pierre.jpeg" alt="Fondateur 1" class="founder-image" />
          <h3>Pierre PERIDY</h3>
          <p>Rôle : Co-fondateur & Podcasteur</p>
        </div>
        <div class="founder-card">
          <img src="../assets/photo%20de%20profil/dorian.jpeg" alt="Fondateur 2" class="founder-image" />
          <h3>Dorian ROBINEAU</h3>
          <p>Rôle : Co-fondateur & Monteur vidéo</p>
        </div>
        <div class="founder-card">
          <img src="../assets/photo%20de%20profil/mathis.jpeg" alt="Fondateur 3" class="founder-image" />
          <h3>Mathis COURTOIS</h3>
          <p>Rôle : Co-fondateur & Community Manager</p>
        </div>
      </div>
    </section>

    <!-- Section : Rejoignez-nous -->
    <section class="section join-us">
      <h2>Rejoignez-nous !</h2>
      <p>
        Vous partagez nos valeurs et souhaitez contribuer à nos projets ? Rejoignez Goat Trotters en tant que membre actif, bienfaiteur ou partenaire !
      </p>
      <router-link to="/contact" class="cta-button">Nous contacter</router-link>
    </section>
  </div>
</template>

<script setup>
// Pas de logique spécifique pour cette page pour l'instant
</script>

<style scoped>
.about-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Arial', sans-serif;
  color: #333;
}

.about-header {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem 0;
  background-color: #f8f8f8;
  border-radius: 8px;
}

.about-header h1 {
  font-size: 2.5rem;
  color: #D4AF37; /* Couleur dorée pour rappeler votre identité visuelle */
  margin-bottom: 1rem;
}

.subtitle {
  font-size: 1.2rem;
  color: #666;
}

.section {
  margin-bottom: 3rem;
}

.section h2 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #2c3e50;
  border-bottom: 2px solid #D4AF37;
  padding-bottom: 0.5rem;
  display: inline-block;
}

.objectives {
  display: flex;
  justify-content: space-between;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.objective-card {
  flex: 1;
  min-width: 250px;
  padding: 1.5rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.objective-card h3 {
  color: #D4AF37;
  margin-bottom: 1rem;
}

.founders {
  display: flex;
  justify-content: space-between;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.founder-card {
  flex: 1;
  min-width: 250px;
  text-align: center;
  padding: 1.5rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.founder-image {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin: 0 auto 1rem;
  border: 3px solid #D4AF37;
}

.join-us {
  text-align: center;
  background-color: #f8f8f8;
  padding: 2rem;
  border-radius: 8px;
}

.cta-button {
  display: inline-block;
  margin-top: 1rem;
  padding: 0.8rem 1.5rem;
  background-color: #D4AF37;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-weight: bold;
  transition: background-color 0.3s;
}

.cta-button:hover {
  background-color: #B8952D;
}
</style>
