package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }
    @PostMapping("/notes")
    public String addAndUpdateNote(@ModelAttribute("noteForm") Note noteForm, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        noteForm.setUserid(user.getUserid());
        if(noteForm.getNoteid() > 0){
            Integer success = noteService.updateNote(noteForm);
            System.out.println("Note update");
            model.addAttribute("success", true);
            return "redirect:/result?success";
        }else {
            noteService.addNote(new Note(noteForm.getNotetitle(),noteForm.getNotedescription(),noteForm.getUserid()),
                    user.getUserid());
            System.out.println("Note Added successfully");
            model.addAttribute("success", true);
            return "redirect:/result?success";
        }
    }

    @GetMapping("/notes/delete")
    public String deleteNote(@RequestParam("noteid") Integer noteid, Model model){
        if(noteid > 0){
            noteService.deleteNote((noteid));
            model.addAttribute("success", true);
            return "redirect:/result?success";
        }
        model.addAttribute("error", true);
        model.addAttribute("error","there is an error to delete the note, plz try again");
        return  "redirect:/result?error";
    }
}
