// 전역 변수 선언
var mapContainer, mapOption, map, geocoder, marker;

// DOM이 로드된 후 실행될 함수
function initMap() {
    mapContainer = document.getElementById('map'); // 'map'은 지도를 표시할 div의 id입니다
    mapOption = {
        center: new daum.maps.LatLng(37.537187, 127.005476),
        level: 5
    };

    map = new daum.maps.Map(mapContainer, mapOption);
    geocoder = new daum.maps.services.Geocoder();
    marker = new daum.maps.Marker({
        position: new daum.maps.LatLng(37.537187, 127.005476),
        map: map
    });
}

function sample5_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.address;
            document.getElementById("sample5_address").value = addr;
            geocoder.addressSearch(data.address, function(results, status) {
                if (status === daum.maps.services.Status.OK) {
                    var result = results[0];
                    var coords = new daum.maps.LatLng(result.y, result.x);
                    mapContainer.style.display = "block";
                    map.relayout();
                    map.setCenter(coords);
                    marker.setPosition(coords)
                }
            });
        }
    }).open();
}

// DOM이 로드된 후 initMap 함수 실행
document.addEventListener('DOMContentLoaded', initMap);