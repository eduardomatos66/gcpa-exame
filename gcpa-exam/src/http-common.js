import axios from "axios"

function getBaseURL() {
    const path = "question";
    let baseURL = process.env.VUE_APP_ROOT_API + ":8000/{path}".replace("{path}", path);
    if (process.env.VUE_APP_ENVIRONMENT === "production") {
        baseURL = "http://{address}:{port}/{path}".replace("{address}", process.env.VUE_APP_ADDRESS)
                                                  .replace("{port}", process.env.VUE_APP_PORT)
                                                  .replace("{path}", path)
    }
    return baseURL;
}

export default axios.create({
    baseURL: getBaseURL(),
    headers: {
        "Content-type": "application/json"
    }
});