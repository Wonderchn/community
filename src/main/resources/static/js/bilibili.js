
        var startX = 0
const images = document.querySelectorAll('header>div>img')

document.querySelector('#header_bi').addEventListener('mousemove', (e)=>{
  let offsetX = e.clientX - startX + 482
  let percentage = offsetX / window.outerWidth
  let offset = 15 * percentage
  let blur = 20
  for (let [index, image] of images.entries()) {
    offset *= 1.3
    let blurValue = (Math.pow(((index / images.length - percentage)), 2) * blur)
    image.style.setProperty('--offset',`${offset}px`)
    image.style.setProperty('--blur',`${blurValue}px`)
  }
})
document.querySelector('#header_bi').addEventListener('mouseover', (e)=>{
  startX = e.clientX
  for (let [index, image] of images.entries()) {
    image.style.transition = 'none'
  }
})

document.querySelector('#header_bi').addEventListener('mouseout', ()=>{
  let offsetX = 482
  let blur = 20
  let percentage = offsetX / window.outerWidth
  let offset = 15 * percentage
  for (let [index, image] of images.entries()) {
    offset *= 1.3
    blurValue = (Math.pow((index / images.length - percentage), 2) * blur)
    image.style.setProperty('--offset',`${offset}px`)
    image.style.setProperty('--blur',`${blurValue}px`)
    image.style.transition = 'all .3s ease'
  }
})
window.addEventListener('load', () => {
  let offsetX = 482
  let blur = 20
  let percentage = offsetX / window.outerWidth
  let offset = 15 * percentage
  for (let [index, image] of images.entries()) {
    offset *= 1.3
    blurValue = (Math.pow((index / images.length - percentage), 2) * blur)
    image.style.setProperty('--offset',`${offset}px`)
    image.style.setProperty('--blur',`${blurValue}px`)
  }
})