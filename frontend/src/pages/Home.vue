<script setup>
const polarstepsUrl = "https://www.polarsteps.com/DorianRobineau/16926536-road-trip-podcast"
</script>

<template>
  <div class="home-container">

    <header class="hero-section">
      <div class="hero-content fade-in-up">
        <span class="badge">Road Trip en Europe</span>
        <h1>Bienvenue chez les <br><span class="highlight">Goat Trotters</span> 🐐</h1>
        <p class="hero-subtitle">
          Découverte, Écologie & Rencontres.<br>
          Explorez l'Europe à travers nos yeux.
        </p>
        <div class="hero-buttons">
          <a href="#tracker" class="btn-primary">Suivre le voyage</a>
          <router-link to="/about" class="btn-secondary">L'Association</router-link>
        </div>
      </div>

      <div class="hero-bg"></div>
    </header>

    <section id="tracker" class="section tracker-section">
      <div class="tracker-card">
        <div class="tracker-info">
          <span class="live-indicator">
            <span class="pulse"></span> EN DIRECT
          </span>
          <h2>Où sommes-nous ?</h2>
          <p>
            Suivez notre position GPS en temps réel et nos photos sur notre tracker officiel.
          </p>
          <a :href="polarstepsUrl" target="_blank" class="btn-polarsteps">
            Voir sur Polarsteps 🗺️
          </a>
        </div>
        <div class="tracker-visual">
          <div class="fake-map-lines"></div>
          <div class="pin">📍</div>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="section-header">
        <h2>Nos Aventures</h2>
        <div class="separator"></div>
      </div>

      <div class="adventures-grid">

        <div class="adventure-card podcast">
          <div class="icon-wrapper">🎙️</div>
          <h3>Podcasts</h3>
          <p>Interviews et découvertes culturelles.</p>
          <a href="https://instagram.com/goat.trotters" target="_blank" class="card-link">
            Voir la page Instagram →
          </a>
        </div>

        <div class="adventure-card vlog">
          <div class="icon-wrapper">🎬</div>
          <h3>Vlogs</h3>
          <p>Vivez le voyage en images.</p>
          <a href="https://youtube.com/@goattrotters" target="_blank" class="card-link">
            Voir la chaîne YouTube →
          </a>
        </div>

        <div class="adventure-card app coming-soon">
          <div class="status-badge">Bientôt</div>

          <div class="icon-wrapper">📱</div>
          <h3>L'App</h3>
          <p>Retrouvez toutes nos datas et la carte interactive directement dans votre poche.</p>

          <span class="card-link disabled">
            En cours de développement ⏳
          </span>
        </div>

      </div>
    </section>

    <section class="section cta-section">
      <div class="cta-content">
        <h2>Participez à l'Aventure !</h2>
        <p>Vos avis comptent. Répondez à nos sondages pour enrichir notre étude.</p>
        <router-link to="/questions" class="btn-gold-pulse">
          Répondre au Sondage
        </router-link>
      </div>
    </section>
  </div>
</template>

<style scoped>

.home-container {
  min-height: 100vh;
  background-color: #1b1336;
  color: white;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  overflow-x: hidden;
}

.hero-section {
  position: relative;
  height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 2rem;
  background: radial-gradient(circle at center, #2a2245 0%, #120c24 100%);
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  opacity: 0.5;
  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
}

.badge {
  background: rgba(212, 175, 55, 0.2);
  color: #D4AF37;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  border: 1px solid #D4AF37;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.hero-section h1 {
  font-size: 4rem;
  margin: 1.5rem 0;
  line-height: 1.1;
  font-weight: 700;
}

.highlight {
  color: #D4AF37;
  text-shadow: 0 0 20px rgba(212, 175, 55, 0.3);
}

.hero-subtitle {
  font-size: 1.5rem;
  color: #ccc;
  margin-bottom: 2.5rem;
  font-weight: 300;
}

.hero-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
}

.tracker-section {
  padding: 4rem 2rem;
  display: flex;
  justify-content: center;
}

.tracker-card {
  background: linear-gradient(135deg, #2a2245 0%, #1b1336 100%);
  width: 100%;
  max-width: 1000px;
  border-radius: 20px;
  display: flex;
  overflow: hidden;
  border: 1px solid #444;
  box-shadow: 0 20px 50px rgba(0,0,0,0.5);
  position: relative;
}

.tracker-info {
  flex: 1;
  padding: 3rem;
  z-index: 2;
}

.live-indicator {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #ff4444;
  font-weight: bold;
  font-size: 0.9rem;
  letter-spacing: 1px;
  margin-bottom: 1rem;
}

.pulse {
  width: 10px; height: 10px;
  background: #ff4444;
  border-radius: 50%;
  box-shadow: 0 0 0 rgba(255, 68, 68, 0.4);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(255, 68, 68, 0.7); }
  70% { box-shadow: 0 0 0 10px rgba(255, 68, 68, 0); }
  100% { box-shadow: 0 0 0 0 rgba(255, 68, 68, 0); }
}

.tracker-info h2 { color: white; font-size: 2.5rem; margin-bottom: 1rem; }
.tracker-info p { color: #aaa; margin-bottom: 2rem; font-size: 1.1rem; }

.btn-polarsteps {
  display: inline-block;
  background: #ff4d4d;
  color: white;
  padding: 15px 30px;
  border-radius: 50px;
  text-decoration: none;
  font-weight: bold;
  transition: transform 0.3s;
}
.btn-polarsteps:hover { transform: translateY(-3px); box-shadow: 0 10px 20px rgba(255, 77, 77, 0.3); }

.tracker-visual {
  flex: 1;
  background: #150f29;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fake-map-lines {
  position: absolute;
  width: 200%; height: 200%;
  background-image: radial-gradient(#444 1px, transparent 1px);
  background-size: 30px 30px;
  opacity: 0.3;
  transform: rotate(15deg);
}

.pin { font-size: 4rem; animation: bounce 2s infinite; }
@keyframes bounce { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-20px); } }

.section { padding: 4rem 2rem; max-width: 1200px; margin: 0 auto; }
.section-header { text-align: center; margin-bottom: 3rem; }
.section-header h2 { font-size: 2.5rem; color: #D4AF37; font-weight: 300; }
.separator { width: 60px; height: 3px; background: #D4AF37; margin: 1rem auto; }

.adventures-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.adventure-card {
  background: #2a2245;
  padding: 2rem;
  border-radius: 16px;
  border: 1px solid #333;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.adventure-card:hover {
  transform: translateY(-10px);
  border-color: #D4AF37;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.icon-wrapper { font-size: 3rem; margin-bottom: 1rem; }
.adventure-card h3 { color: white; margin-bottom: 0.5rem; font-size: 1.5rem; }
.adventure-card p { color: #aaa; margin-bottom: 1.5rem; min-height: 50px; }

.card-link {
  color: #D4AF37;
  text-decoration: none;
  font-weight: bold;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  transition: gap 0.2s;
}
.card-link:hover { gap: 10px; }

.adventure-card.coming-soon {
  border: 1px dashed #666;
  background: rgba(42, 34, 69, 0.5);
  position: relative;
  cursor: default;
}

.adventure-card.coming-soon:hover {
  transform: none;
  border-color: #666;
  box-shadow: none;
}

.status-badge {
  position: absolute;
  top: 15px;
  right: 15px;
  background: #D4AF37;
  color: #1b1336;
  font-size: 0.75rem;
  font-weight: bold;
  padding: 4px 10px;
  border-radius: 50px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.card-link.disabled {
  color: #888;
  font-style: italic;
  font-weight: normal;
  cursor: not-allowed;
  opacity: 0.8;
}

.card-link.disabled:hover {
  text-decoration: none;
  color: #888;
}

.cta-section { margin-top: 4rem; text-align: center; }
.cta-content {
  background: linear-gradient(45deg, #D4AF37, #B8952D);
  padding: 4rem;
  border-radius: 20px;
  color: #1b1336;
}

.cta-content h2 { font-size: 2.5rem; margin-bottom: 1rem; font-weight: 800; }
.cta-content p { font-size: 1.2rem; margin-bottom: 2rem; font-weight: 500; }

.btn-primary {
  background: #D4AF37; color: #1b1336;
  padding: 12px 30px; border-radius: 50px; text-decoration: none; font-weight: bold;
  transition: all 0.3s;
}
.btn-primary:hover { background: white; transform: scale(1.05); }

.btn-secondary {
  background: transparent; color: white; border: 1px solid white;
  padding: 12px 30px; border-radius: 50px; text-decoration: none; font-weight: bold;
  transition: all 0.3s;
}
.btn-secondary:hover { background: white; color: #1b1336; }

.btn-gold-pulse {
  background: #1b1336; color: white;
  padding: 15px 40px; border-radius: 50px; text-decoration: none; font-weight: bold;
  font-size: 1.2rem;
  box-shadow: 0 0 0 0 rgba(27, 19, 54, 0.7);
  animation: pulse-dark 2s infinite;
  display: inline-block;
}

@keyframes pulse-dark {
  0% { box-shadow: 0 0 0 0 rgba(27, 19, 54, 0.4); }
  70% { box-shadow: 0 0 0 20px rgba(27, 19, 54, 0); }
  100% { box-shadow: 0 0 0 0 rgba(27, 19, 54, 0); }
}

.fade-in-up { animation: fadeInUp 1s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(40px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 768px) {
  .hero-section h1 { font-size: 2.5rem; }
  .tracker-card { flex-direction: column; }
  .tracker-visual { height: 200px; width: 100%; }
}
</style>
