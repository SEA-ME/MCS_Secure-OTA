export function naverMap() {
  const position = new naver.maps.LatLng(37.576181759845724, 126.97685875394319)
  const mapOptions = {
    scrollWheel: false,
    center: position,
    zoom: 17
  }

  const map = new naver.maps.Map('map', mapOptions)
  const marker = new naver.maps.Marker({ position, map })
}
