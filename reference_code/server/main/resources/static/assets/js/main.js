import { bs } from './modules/bsComponents.js'
import { shadow } from './modules/shadow.js'
import { wowAnimate } from './modules/wowAnimate.js'
import { scroll } from './modules/scroll.js'
import { naverMap } from './modules/naverMap.js'

bs()
if (document.querySelector('#shadow-box')) {
  shadow()
}

if (document.querySelector('.wow')) {
  wowAnimate()
}

if (document.querySelector('#scrolltoTop')) {
  scroll()
}

if (document.querySelector('#map')) {
  naverMap()
}
