package com.roxana.recipeapp.comment

import androidx.lifecycle.SavedStateHandle
import com.roxana.recipeapp.AddCommentNode
import com.roxana.recipeapp.domain.comment.AddCommentUseCase
import com.roxana.recipeapp.helpers.MainCoroutineRule
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddCommentViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val addCommentUseCase: AddCommentUseCase = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private lateinit var viewModel: AddCommentViewModel

    @Before
    fun setUp() {
        viewModel = AddCommentViewModel(addCommentUseCase, savedStateHandle)
    }

    @Test
    fun setStateCanSave_when_changeComment_givenNotBlank() = runTest {
        // Given
        val comment = "comment"

        // When
        viewModel.onChangeComment(comment)

        // Then
        viewModel.state.value shouldBe AddCommentState(comment, canSave = true, isValidated = false)
    }

    @Test
    fun setStateCanNotSave_when_changeComment_givenBlank() = runTest {
        // Given
        val comment = "  "

        // When
        viewModel.onChangeComment(comment)

        // Then
        viewModel.state.value shouldBe AddCommentState(
            comment,
            canSave = false,
            isValidated = false
        )
    }

    @Test
    fun doNothing_when_saveComment_givenBlankComment() = runTest {
        // Given
        val comment = "  "
        val recipeId = 1234
        viewModel.onChangeComment(comment)
        every { savedStateHandle.get<Int>(AddCommentNode.KEY_ID)!! } returns recipeId

        // When
        viewModel.onSaveComment()

        // Then
        viewModel.state.value.isValidated.shouldBeFalse()
        coVerify(exactly = 0) { addCommentUseCase(any()) }
    }

    @Test
    fun saveComment_when_saveComment_givenNotBlankComment() = runTest {
        // Given
        val comment = "comment"
        val recipeId = 1234
        viewModel.onChangeComment(comment)
        every { savedStateHandle.get<Int>(AddCommentNode.KEY_ID)!! } returns recipeId
        coEvery { addCommentUseCase(any()) } returns Result.success(Unit)

        // When
        viewModel.onSaveComment()

        // Then
        viewModel.state.value.isValidated.shouldBeTrue()
        coVerify { addCommentUseCase(AddCommentUseCase.Input(recipeId, comment)) }
    }

    @Test
    fun resetOnValidate_when_onValidateDone() = runTest {
        // Given
        val comment = "comment"
        val recipeId = 1234
        viewModel.onChangeComment(comment)
        every { savedStateHandle.get<Int>(AddCommentNode.KEY_ID)!! } returns recipeId
        coEvery { addCommentUseCase(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onSaveComment()
        viewModel.state.value.isValidated.shouldBeTrue()
        viewModel.onValidateDone()
        viewModel.state.value.isValidated.shouldBeFalse()
    }
}
