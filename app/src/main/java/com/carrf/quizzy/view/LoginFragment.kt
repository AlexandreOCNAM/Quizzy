package com.carrf.quizzy.view

import android.content.Context
import com.carrf.quizzy.db.AppDatabase
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.carrf.quizzy.R
import com.carrf.quizzy.data.entities.Question
import com.carrf.quizzy.data.entities.Quiz
import com.carrf.quizzy.data.entities.User

class LoginFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "quiz-db"
        ).allowMainThreadQueries().build()

        sharedPreferences = requireActivity().getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)

        val usernameEditText: EditText = view.findViewById(R.id.username)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.login_button)
        val quizzesList: ListView = view.findViewById(R.id.quizzes_list)

        val currentUser = getCurrentUser()
        if (currentUser != null) {
            quizzesList.visibility = View.VISIBLE
            loadQuizzes(quizzesList)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = db.userDao().getUserByUsernameAndPassword(username, password)
            if (user != null) {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                saveCurrentUser(user)
                quizzesList.visibility = View.VISIBLE
                loadQuizzes(quizzesList)
            } else {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun loadQuizzes(listView: ListView) {
        val quizzes = db.quizDao().getAllQuizzes()
        val quizTitles = quizzes.map { it.title }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, quizTitles)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuiz = quizzes[position]
            val fragment = QuizFragment().apply {
                arguments = Bundle().apply {
                    putInt("QUIZ_ID", selectedQuiz.quizId)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun saveCurrentUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putInt("USER_ID", user.userId)
        editor.apply()
    }

    private fun getCurrentUser(): User? {
        val userId = sharedPreferences.getInt("USER_ID", -1)
        return if (userId != -1) {
            db.userDao().getUserById(userId)
        } else {
            null
        }
    }
}
