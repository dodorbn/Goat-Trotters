<script setup>
import { ref } from 'vue'

import imgPierre from '../assets/photo de profil/pierre.jpeg'
import imgDorian from '../assets/photo de profil/dorian.jpeg'
import imgMathis from '../assets/photo de profil/mathis.jpeg'

const founders = ref([
  {
    id: 1,
    name: 'Pierre PERIDY',
    role: 'Co-fondateur & Podcasteur',
    image: imgPierre,
    bio: "Passionné par la parole, Pierre donne la voix au projet via les podcasts et interviews.",
    flipped: false
  },
  {
    id: 2,
    name: 'Dorian ROBINEAU',
    role: 'Co-fondateur & Monteur',
    image: imgDorian,
    bio: "L'œil artistique. Dorian transforme nos kilomètres en récits visuels captivants.",
    flipped: false
  },
  {
    id: 3,
    name: 'Mathis COURTOIS',
    role: 'Co-fondateur & Caméraman',
    image: imgMathis,
    bio: "L'homme de terrain. Mathis capture l'instant présent et gère la communauté.",
    flipped: false
  }
])

const toggleCard = (id) => {
  const founder = founders.value.find(f => f.id === id)
  if (founder) {
    founder.flipped = !founder.flipped
  }
}
</script>

<template>
  <div class="page-container">

    <header class="header">
      <h1>À propos de <span class="highlight">Goat Trotters</span></h1>
      <p class="subtitle">Voyage, Écologie & Rencontres</p>
      <div class="separator"></div>
    </header>

    <section class="intro-section fade-in">
      <p class="intro-text">
        <span class="highlight-text">Goat Trotters</span> est une association née de la passion de trois amis pour les voyages,
        les rencontres interculturelles et le partage d'expériences authentiques.
      </p>
      <p class="intro-subtext">
        À travers nos <strong>road trips en Europe</strong>, nous documentons nos aventures sous forme de podcasts et de vlogs.
      </p>
    </section>

    <section class="section">
      <h2>Nos Objectifs</h2>
      <div class="grid-cards">
        <div class="card objective">
          <div class="icon">🌍</div>
          <h3>Sensibiliser</h3>
          <p>Mettre en lumière la diversité culturelle européenne.</p>
        </div>
        <div class="card objective">
          <div class="icon">💡</div>
          <h3>Inspirer</h3>
          <p>Encourager les jeunes à voyager autrement.</p>
        </div>
        <div class="card objective">
          <div class="icon">🎙️</div>
          <h3>Partager</h3>
          <p>Diffuser nos aventures et nos données.</p>
        </div>
      </div>
    </section>

    <section class="section">
      <h2>L'Équipe Fondatrice</h2>
      <div class="founders-grid">
        <div
          v-for="f in founders"
          :key="f.id"
          class="founder-card"
          :class="{ flipped: f.flipped }"
          @click="toggleCard(f.id)"
        >
          <div class="flip-inner">

            <div class="card-face card-front">
              <div class="img-wrapper">
                <img :src="f.image" :alt="f.name" />
              </div>
              <h3>{{ f.name.split(' ')[0] }}</h3>
              <span class="lastname">{{ f.name.split(' ')[1] }}</span>

              <div class="spacer"></div>
              <span class="role">{{ f.role }}</span>
              <div class="flip-icon">↻</div>
            </div>

            <div class="card-face card-back">
              <h3>{{ f.name }}</h3>
              <div class="mini-separator"></div>
              <p>{{ f.bio }}</p>
            </div>

          </div>
        </div>
      </div>
    </section>

    <section class="join-us-banner">
      <div class="banner-content">
        <h2>Rejoignez l'aventure !</h2>
        <p>
          Vous partagez nos valeurs ? Devenez membre actif ou partenaire.
        </p>
        <router-link to="/contact" class="cta-button">Nous contacter</router-link>
      </div>
    </section>

  </div>
</template>

<style scoped>
/* --- GLOBALS --- */
/* (Je remets box-sizing: border-box globalement pour éviter les soucis futurs) */
*, *::before, *::after {
  box-sizing: border-box;
}

.page-container {
  min-height: 100vh;
  background-color: #1b1336;
  color: white;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 4rem 2rem;
  display: flex; flex-direction: column; align-items: center;
}
.header { text-align: center; margin-bottom: 3rem; }
.header h1 { font-size: 2.5rem; margin-bottom: 0.5rem; font-weight: 300; }
.highlight { color: #D4AF37; font-weight: 600; }
.subtitle { color: #aaa; font-size: 1rem; letter-spacing: 2px; text-transform: uppercase; }
.separator { width: 60px; height: 3px; background: #D4AF37; margin: 1rem auto; border-radius: 2px; }

.intro-section { max-width: 700px; text-align: center; margin-bottom: 4rem; line-height: 1.6; color: #e0e0e0; }
.intro-text { font-size: 1.1rem; margin-bottom: 1rem; }
.section h2 { text-align: center; font-size: 1.8rem; color: #D4AF37; margin-bottom: 2rem; font-weight: 300; }

.grid-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem; max-width: 1000px; width: 100%; margin-bottom: 4rem; }
.card { background: #2a2245; padding: 1.5rem; border-radius: 12px; border: 1px solid #444; text-align: center; transition: transform 0.3s; }
.card:hover { transform: translateY(-5px); border-color: #D4AF37; }
.icon { font-size: 2.5rem; margin-bottom: 1rem; }
.card h3 { color: #fff; margin-bottom: 0.5rem; font-size: 1.2rem; }
.card p { color: #aaa; font-size: 0.9rem; }


/* --- FONDATEURS (CORRIGÉ) --- */

.founders-grid {
  display: flex;
  justify-content: center;
  gap: 2rem; /* Espace suffisant */
  flex-wrap: wrap;
  margin-bottom: 4rem;
}

.founder-card {
  width: 240px;
  height: 320px;
  perspective: 1500px; /* Profondeur augmentée pour réduire l'effet de "swing" large */
  cursor: pointer;
  background-color: transparent;
  position: relative;
  z-index: 1; /* Niveau de base */
}

/* ⚡️ CORRECTION CHEVAUCHEMENT : La carte active passe au premier plan */
.founder-card:hover, .founder-card.flipped {
  z-index: 10;
}

.flip-inner {
  position: relative;
  width: 100%;
  height: 100%;
  text-align: center;
  transition: transform 0.6s ease-in-out;
  transform-style: preserve-3d;
  /* ⚡️ CORRECTION TRANSLATION : Centre strict */
  transform-origin: 50% 50%;
}

.founder-card.flipped .flip-inner {
  transform: rotateY(180deg);
}

/* --- Style des faces --- */
.card-face {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;
  border-radius: 12px;

  box-sizing: border-box;
  padding: 1.5rem;

  display: flex;
  flex-direction: column;
  align-items: center;
  background: #2a2245;
  border: 1px solid #444;
  box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

.founder-card:hover .card-face {
  border-color: #666;
}

/* --- FACE AVANT --- */
.card-front {
  z-index: 2;
  transform: rotateY(0deg);
}

.img-wrapper {
  width: 110px; height: 110px; margin-bottom: 1rem; border-radius: 50%; padding: 3px; margin-top: 1rem;
  background: linear-gradient(135deg, #D4AF37 0%, #1b1336 100%);
}
.card-front img { width: 100%; height: 100%; border-radius: 50%; object-fit: cover; border: 2px solid #2a2245; }

.card-front h3 { font-size: 1.3rem; margin: 0; color: white; line-height: 1; }
.card-front .lastname { font-size: 1rem; text-transform: uppercase; letter-spacing: 1px; color: #ccc; margin-bottom: 0.5rem; }

.spacer { flex-grow: 1; }

.card-front .role { color: #D4AF37; font-style: italic; font-size: 0.85rem; margin-bottom: 0.5rem; }
.flip-icon { color: #555; font-size: 1.2rem; transition: transform 0.3s; }
.founder-card:hover .flip-icon { color: #D4AF37; transform: rotate(180deg); }

/* --- FACE ARRIÈRE --- */
.card-back {
  transform: rotateY(180deg);
  justify-content: center;
  background: linear-gradient(160deg, #322850 0%, #251d3d 100%);
}

.card-back h3 { color: #D4AF37; font-size: 1.2rem; margin-bottom: 0.5rem; }
.mini-separator { width: 30px; height: 2px; background: #D4AF37; margin-bottom: 1rem; }
.card-back p { color: #e0e0e0; font-size: 0.9rem; line-height: 1.5; }


/* --- CTA --- */
.join-us-banner { background: linear-gradient(135deg, #2a2245 0%, #1b1336 100%); width: 100%; max-width: 800px; padding: 2.5rem; border-radius: 16px; text-align: center; border: 1px solid #D4AF37; }
.join-us-banner h2 { color: white; margin-bottom: 0.5rem; font-size: 1.5rem; }
.join-us-banner p { color: #ccc; margin-bottom: 1.5rem; font-size: 1rem; }
.cta-button { display: inline-block; background-color: #D4AF37; color: #1b1336; padding: 10px 25px; border-radius: 50px; text-decoration: none; font-weight: bold; transition: all 0.3s ease; }
.cta-button:hover { background-color: white; color: #D4AF37; transform: scale(1.05); }

.fade-in { animation: fadeIn 1s ease-out; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
@media (max-width: 768px) { .header h1 { font-size: 2rem; } .grid-cards { grid-template-columns: 1fr; } .founders-grid { gap: 1.5rem; } }
</style>
