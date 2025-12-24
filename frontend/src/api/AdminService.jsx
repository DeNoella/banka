import api from "./axios";

export const createManager = async (data) => {
  const response = await api.post('/admin/create-manager', data)
  return response.data
}

export const getStats = async () => {
  const response = await api.get('/admin/stats')
  return response.data
};
