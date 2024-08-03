import axios from "@/axios";

const notesService = {
  getAllNotes() {
    return axios.get("/notes");
  },

  getNoteById(id) {
    return axios.get(`/notes/${id}`);
  },

  createNote(noteData) {
    return axios.post("/notes", noteData);
  },

  updateNote(id, noteData) {
    return axios.put(`/notes/${id}`, noteData);
  },

  deleteNote(id) {
    return axios.delete(`/notes/${id}`);
  },

  getAllActiveNotes() {
    return axios.get("/notes/active");
  },

  getAllArchivedNotes() {
    return axios.get("/notes/archived");
  },
};

export default notesService;
