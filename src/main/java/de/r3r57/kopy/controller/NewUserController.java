package de.r3r57.kopy.controller;

import de.r3r57.kopy.model.MainModel;

public class NewUserController {

	private MainModel model;

	public NewUserController(MainModel model) {
		this.model = model;

	}

	public void newUser(String name) {

		model.addUser(name);

	}

}
