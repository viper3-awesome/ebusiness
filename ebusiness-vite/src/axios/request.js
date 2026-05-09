import axios from 'axios'

const myAxios = axios.create({
    baseURL: 'http://localhost:8443/eBusiness',
    timeout: 5000
})

export default myAxios