package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Integer addNote(Note note, Integer userid){
        return noteMapper.insertNote(new Note(note.getNotetitle(), note.getNotedescription(), userid));
    }

    public Integer updateNote(Note note){
        return noteMapper.updateNote(note);
    }

    public Integer deleteNote(Integer noteid){
        return noteMapper.deleteNote(noteid);
    }

    public List<Note> getNotes(Integer userid){
        List<Note> notes = noteMapper.getNotes(userid);
        return notes;
    }
}
