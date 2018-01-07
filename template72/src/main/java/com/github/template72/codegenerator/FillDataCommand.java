package com.github.template72.codegenerator;

import com.github.template72.syntax.TemplateSyntax;

public class FillDataCommand {
	private boolean objVar = false;
	private String name;
	private String model;
	private String comment;
	private boolean each = false;
	private String listVar;
	private String block;

	public boolean isVar() {
		return !objVar && !each;
	}

	public boolean isObjVar() {
		return objVar;
	}

	public void setObjVar(boolean objVar) {
		this.objVar = objVar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGetter() {
		return "get" + getName().substring(0, 1).toUpperCase() + getName().substring(1) + "()";
	}

	public boolean isEach() {
		return each;
	}

	public void setEach(boolean each) {
		this.each = each;
	}

	public String getLastName() {
		int o = getName().lastIndexOf(TemplateSyntax.internalFieldSep);
		if (o >= 0) {
			return getName().substring(o + TemplateSyntax.internalFieldSep.length());
		} else {
			return getName();
		}
	}

	public String getListVar() {
		return listVar;
	}

	public void setListVar(String listVar) {
		this.listVar = listVar;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (each ? 1231 : 1237);
		result = prime * result + ((listVar == null) ? 0 : listVar.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (objVar ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FillDataCommand other = (FillDataCommand) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (each != other.each)
			return false;
		if (listVar == null) {
			if (other.listVar != null)
				return false;
		} else if (!listVar.equals(other.listVar))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objVar != other.objVar)
			return false;
		return true;
	}
}
