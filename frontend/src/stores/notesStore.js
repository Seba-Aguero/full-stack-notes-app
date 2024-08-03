import { defineStore } from "pinia";
import notesService from "@/services/notesService";

export const useNotesStore = defineStore("notes", {
  state: () => ({
    notes: [],
  }),
  actions: {
    async fetchNotes() {
      try {
        const response = await notesService.getAllNotes();
        this.notes = response.data;
      } catch (error) {
        console.error("Error fetching notes:", error);
      }
    },
    async createNote(noteData) {
      try {
        const response = await notesService.createNote(noteData);
        this.notes.push(response.data);
      } catch (error) {
        console.error("Error adding note:", error);
      }
    },
    async updateNote(noteData) {
      try {
        const response = await notesService.updateNote(noteData.id, noteData);
        const index = this.notes.findIndex((note) => note.id === noteData.id);
        if (index !== -1) {
          this.notes[index] = response.data;
        }
      } catch (error) {
        console.error("Error updating note:", error);
      }
    },
    async deleteNote(noteId) {
      try {
        await notesService.deleteNote(noteId);
        this.notes = this.notes.filter((note) => note.id !== noteId);
      } catch (error) {
        console.error("Error deleting note:", error);
      }
    },
    async toggleArchive(noteId) {
      const note = this.notes.find((note) => note.id === noteId);
      if (note) {
        try {
          const response = await notesService.updateNote(noteId, {
            ...note,
            archived: !note.archived,
          });
          const index = this.notes.findIndex((n) => n.id === noteId);
          if (index !== -1) {
            this.notes[index] = response.data;
          }
        } catch (error) {
          console.error("Error toggling archive status:", error);
        }
      }
    },
  },
});
