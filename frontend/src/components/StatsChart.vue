<script setup lang="ts">
import { computed } from 'vue'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  ArcElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import { Bar, Pie } from 'vue-chartjs'
import { getColorForText } from '@/assets/colors'

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Title, Tooltip, Legend)

const props = defineProps<{
  stats: any[],
  questionType?: string
}>()

const chartData = computed(() => {
  if (props.questionType === 'SCORE') {
    const labels = props.stats.map(s => s.country)
    const averages = props.stats.map(stat => {
      const counts = stat.counts || {}
      let total = 0, sum = 0
      Object.entries(counts).forEach(([score, count]) => {
        sum += Number(score) * Number(count)
        total += Number(count)
      })
      return total === 0 ? 0 : (sum / total).toFixed(2)
    })
    return {
      labels,
      datasets: [{
        label: 'Moyenne / 10',
        backgroundColor: '#D4AF37',
        data: averages,
        borderRadius: 4
      }]
    }
  } else if (props.questionType === 'CHOICE' || props.questionType === 'MULTIPLE_CHOICE') {
    const globalCounts: Record<string, number> = {}
    props.stats.forEach(stat => {
      if (stat.counts) {
        Object.entries(stat.counts).forEach(([answer, count]) => {
          globalCounts[answer] = (globalCounts[answer] || 0) + Number(count)
        })
      }
    })
    const labels = Object.keys(globalCounts).sort()
    const data = labels.map(label => globalCounts[label])
    const backgroundColor = labels.map(label => getColorForText(label))

    return {
      labels,
      datasets: [{
        data: data,
        backgroundColor: backgroundColor,
        borderWidth: 2,
        borderColor: '#2a2245'
      }]
    }
  }
  return { labels: [], datasets: [] }
})

const chartOptions = computed(() => {
  const isPie = props.questionType === 'CHOICE' || props.questionType === 'MULTIPLE_CHOICE'
  return {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: { color: 'white', font: { size: 13 } },
        position: isPie ? 'bottom' : 'top'
      },
      title: {
        display: true,
        text: isPie ? 'Répartition Globale (Total Europe)' : 'Moyenne par Pays',
        color: '#aaa',
        font: { size: 16 }
      },
      tooltip: {
        callbacks: {
          label: (context: any) => {
            const val = context.raw
            if (isPie) {
              const total = context.dataset.data.reduce((a: any, b: any) => a + b, 0)
              const percent = Math.round((val / total) * 100)
              return ` ${context.label}: ${percent}% (${val} votes)`
            }
            return ` Note: ${val}/10`
          }
        }
      }
    },
    scales: isPie ? {} : {
      x: { ticks: { color: 'white' }, grid: { display: false } },
      y: { beginAtZero: true, max: 10, ticks: { color: 'white' }, grid: { color: '#333' } }
    }
  }
})
</script>

<template>
  <div class="chart-root">
    <div v-if="questionType === 'CHOICE' || questionType === 'MULTIPLE_CHOICE'" class="pie-wrapper">
      <div class="pie-sizer">
        <Pie :data="chartData" :options="chartOptions" />
      </div>
    </div>

    <div v-else-if="questionType === 'SCORE'" class="bar-wrapper">
      <Bar :data="chartData" :options="chartOptions" />
    </div>

    <div v-else class="no-chart">Pas de graphique</div>
  </div>
</template>

<style scoped>
.chart-root {
  width: 100%;
  height: 100%;
}

.pie-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
}

.pie-sizer {
  position: relative;
  width: 100%;
  max-width: 450px;
  height: 100%;
}

.bar-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
}

.no-chart {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #aaa;
}
</style>
