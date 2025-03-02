import axios from 'axios';

const API_URL = 'http://localhost:8080/facilities';

export default {
  getFacilityCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getFacilities(example) {
    return axios.get(`${API_URL}/list`, { params: example });
  },
  getFacilityById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addFacility(facility) {
    return axios.post(`${API_URL}/`, facility);
  },
  addFacilitySelective(facility) {
    return axios.post(`${API_URL}/selective`, facility);
  },
  updateFacility(id, facility) {
    return axios.put(`${API_URL}/${id}`, facility);
  },
  updateFacilitySelective(id, facility) {
    return axios.patch(`${API_URL}/${id}/selective`, facility);
  },
  deleteFacility(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
