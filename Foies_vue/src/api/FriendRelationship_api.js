import axios from 'axios';

const API_URL = 'http://localhost:8080/friend-relationships';

export default {
  getFriendRelationshipCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getFriendRelationships(example) {
    return axios.get(`${API_URL}/list`, { params: example });
  },
  getFriendRelationshipById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addFriendRelationship(friendRelationship) {
    return axios.post(`${API_URL}/`, friendRelationship);
  },
  addFriendRelationshipSelective(friendRelationship) {
    return axios.post(`${API_URL}/selective`, friendRelationship);
  },
  updateFriendRelationship(id, friendRelationship) {
    return axios.put(`${API_URL}/${id}`, friendRelationship);
  },
  updateFriendRelationshipSelective(id, friendRelationship) {
    return axios.patch(`${API_URL}/${id}/selective`, friendRelationship);
  },
  deleteFriendRelationship(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
