import api from "./axios";

export const getStats = async () => {
    const response = await api.get('/manager/stats')
    return response.data
}
