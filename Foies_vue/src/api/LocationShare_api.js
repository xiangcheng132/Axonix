import axios from 'axios';

const API_URL = 'http://localhost:8080/location-shares';

export default {
    getLocationShareCount(example) {
        return axios.get(`${API_URL}/count`, { params: example });
    },
    getLocationShares(example) {
        return axios.get(`${API_URL}/list`, { params: example });
    },
    getLocationShareById(id) {
        return axios.get(`${API_URL}/${id}`);
    },
    addLocationShare(locationShare) {
        return axios.post(`${API_URL}/`, locationShare);
    },
    addLocationShareSelective(locationShare) {
        return axios.post(`${API_URL}/selective`, locationShare);
    },
    updateLocationShare(id, locationShare) {
        return axios.put(`${API_URL}/${id}`, locationShare);
    },
    updateLocationShareSelective(id, locationShare) {
        return axios.patch(`${API_URL}/${id}/selective`, locationShare);
    },
    deleteLocationShare(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
};
