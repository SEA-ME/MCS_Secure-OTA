export function scroll() {
  const scrollTop = document.querySelector('#scrolltoTop')
  const primaryNavbar = document.querySelector('#primary-navbar')

  scrollTop.addEventListener('click', () => window.scroll({ top: 0, behavior: 'smooth' }))
  window.onscroll = () => scrollFunction()

  function scrollFunction() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      scrollTop.style.display = 'block'
      primaryNavbar.classList.add('rgba-black-strong')
      primaryNavbar.classList.remove('rgba-black-light')
    } else {
      scrollTop.style.display = 'none'
      primaryNavbar.classList.remove('rgba-black-strong')
      primaryNavbar.classList.add('rgba-black-light')
    }
  }
}
