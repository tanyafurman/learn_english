package edu.furman.english;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.furman.english.data.User;

public class UserManager {

	private static UserManager instance;

	private final File file;

	private UserManager(File file) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		this.file = file;
	}

	public static UserManager getInstance() {
		return getInstance(null);
	}

	public static UserManager getInstance(File f) {
		if (instance == null) {
			if (f == null) {
				String basePath = System.getProperty("user.dir") + File.separator + "englishApp" + File.separator + "users";
				f = new File(basePath);
				if (!f.exists()) {
					f.getParentFile().mkdirs();
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			instance = new UserManager(f);
		}
		return instance;
	}

	/**
	 * 
	 * @param name
	 * @param surname
	 * @param login
	 * @param pass
	 * @return false if login is already used
	 */
	public boolean createUser(String name, String surname, String login, String pass) {
		List<User> users = load();
		User user = new User(name, surname, login, pass);
		if (users.contains(user)) {
			return false;
		}

		boolean result = users.add(user);

		save(users);
		return result;
	}

	public User login(String login, String pass) {
		List<User> users = load();
		User user = new User(null, null, login, pass);
		int index = users.indexOf(user);
		if (index == -1) {
			return null;
		}
		return users.get(index);
	}

	public void saveUser(User user) {
		List<User> users = load();
		int index = users.indexOf(user);
		if (index == -1) {
			users.add(user);
		} else {
			users.set(index, user);
		}
		save(users);
	}

	protected List<User> load() {
		ArrayList<User> result = new ArrayList<>();
		ObjectInputStream ois = null;
		try {
			FileInputStream is = new FileInputStream(file);
			ois = new ObjectInputStream(is);
			Object o = null;

			while ((o = ois.readObject()) != null) {
				result.add((User)o);
			}
		} catch (EOFException eofEx) {
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	protected void save(List<User> users) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream os = new FileOutputStream(file);
			oos = new ObjectOutputStream(os);
			for (User u : users) {
				oos.writeObject(u);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
