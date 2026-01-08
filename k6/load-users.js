import http from "k6/http";
import { check, sleep } from "k6";

// Настройки нагрузки (можешь менять)
export const options = {
    vus: 5,              // параллельные "пользователи"
    duration: "10m",       // сколько гонять
    thresholds: {
        http_req_failed: ["rate<0.01"],     // <1% ошибок
        http_req_duration: ["p(95)<500"],   // 95% запросов быстрее 500ms
    },
};

const HOST = __ENV.HOST || "http://localhost:8088";
const USERS_MIN_ID = 1;
const USERS_MAX_ID = 35;

// Случайное целое [min..max]
function randInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// Случайная пауза в секундах [min..max]
function randSleep(minSeconds, maxSeconds) {
    sleep(Math.random() * (maxSeconds - minSeconds) + minSeconds);
}

export default function () {
    const userId = randInt(USERS_MIN_ID, USERS_MAX_ID);

    // 1) Иногда запрашиваем список пользователей
    // (например, 30% итераций)
    if (Math.random() < 0.3) {
        const resList = http.get(`${HOST}/api/users`, {
            headers: { Accept: "application/json" },
            tags: { endpoint: "GET_/api/users" },
        });

        check(resList, {
            "list: status 200": (r) => r.status === 200,
        });

        // короткая пауза после списка
        randSleep(0.1, 0.6);
    }

    // 2) Запрашиваем конкретного пользователя
    const resUser = http.get(`${HOST}/api/users/${userId}`, {
        headers: { Accept: "application/json" },
        tags: { endpoint: "GET_/api/users/{id}" },
    });

    check(resUser, {
        "user: status 200": (r) => r.status === 200,
    });

    // 3) Рандомная "think time" пауза между итерациями
    randSleep(0.2, 1.5);
}
