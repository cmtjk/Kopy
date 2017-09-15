package de.r3r57.kopy.controller;

import de.r3r57.kopy.model.MainModel;

public class NewEventController {

	private MainModel model;

	public NewEventController(MainModel model) {
		this.model = model;
	}

	public void addEvent(String event) {
		model.addEvent(event);

	}

}
