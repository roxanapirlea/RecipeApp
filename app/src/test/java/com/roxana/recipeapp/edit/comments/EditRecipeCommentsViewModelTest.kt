package com.roxana.recipeapp.edit.comments

import com.roxana.recipeapp.domain.editrecipe.GetCommentsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetCommentsUseCase
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.helpers.MainCoroutineRule
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditRecipeCommentsViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private val isExistingUC: IsRecipeExistingUseCase = mockk(relaxed = true)
    private val getCommentsUC: GetCommentsUseCase = mockk(relaxed = true)
    private val setCommentsUC: SetCommentsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRecipeCommentsViewModel

    private val comments = listOf(CreationComment("Comment 2", 1), CreationComment("Comment 1", 0))

    @Before
    fun setUp() {
        coEvery { isExistingUC(null) } returns Result.success(true)
        every { getCommentsUC(null) } returns flow { emit(Result.success(comments)) }
    }

    @Test
    fun setExistingRecipe_when_init_given_existingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(true)

        // When
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeTrue()
    }

    @Test
    fun setNewRecipe_when_init_given_noExistingRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)

        // When
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setNewRecipe_when_init_given_existingRecipeError() {
        // Given
        coEvery {
            isExistingUC(null)
        } returns Result.failure(IllegalAccessException())

        // When
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // Then
        viewModel.state.value.isExistingRecipe.shouldBeFalse()
    }

    @Test
    fun setComments_when_init() {
        // Given
        coEvery { getCommentsUC(null) } returns flow { emit(Result.success(comments)) }

        // When
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // Then
        viewModel.state.value.comments shouldContainInOrder listOf("Comment 1", "Comment 2")
    }

    @Test
    fun setNoComments_when_init_given_error() {
        // Given
        coEvery {
            getCommentsUC(null)
        } returns flow { emit(Result.failure(IllegalStateException())) }

        // When
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // Then
        viewModel.state.value.comments.shouldBeEmpty()
    }

    @Test
    fun changeComment_when_onCommentChanged() {
        // Given
        val comment = "New comment"
        val emptyComment = "  "
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.editingComment shouldBe comment
        viewModel.state.value.canAddEditingComment.shouldBeTrue()

        viewModel.onCommentChanged(emptyComment)
        viewModel.state.value.editingComment shouldBe emptyComment
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
    }

    @Test
    fun saveComment_when_onSaveComment_given_validComment() {
        // Given
        val comment = "New comment"
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.canAddEditingComment.shouldBeTrue()
        viewModel.onSaveComment()
        viewModel.state.value.editingComment shouldBe ""
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.state.value.comments shouldContainInOrder listOf(
            "Comment 1",
            "Comment 2",
            comment
        )
    }

    @Test
    fun doNothing_when_onSaveComment_given_invalidComment() {
        // Given
        val comment = " "
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.onSaveComment()
        viewModel.state.value.editingComment shouldBe comment
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.state.value.comments shouldNotContain comment
    }

    @Test
    fun saveComment_when_onCommentDone_given_validComment() {
        // Given
        val comment = "New comment"
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.canAddEditingComment.shouldBeTrue()
        viewModel.onCommentDone()
        viewModel.state.value.editingComment shouldBe ""
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.state.value.comments shouldContainInOrder listOf(
            "Comment 1",
            "Comment 2",
            comment
        )
    }

    @Test
    fun goForwardAndSaveState_when_onCommentDone_given_invalidCommentAndNoError() {
        // Given
        val comment = " "
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.onCommentDone()
        viewModel.state.value.editingComment shouldBe " "
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.state.value.comments shouldContainInOrder listOf(
            "Comment 1",
            "Comment 2"
        )
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setCommentsUC(comments.sortedBy { it.ordinal }) }
    }

    @Test
    fun goForward_when_onCommentDone_given_invalidCommentAndError() {
        // Given
        val comment = " "
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.onCommentDone()
        viewModel.state.value.editingComment shouldBe " "
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.state.value.comments shouldContainInOrder listOf(
            "Comment 1",
            "Comment 2"
        )
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun goForwardAndSaveState_when_onCommentDone_given_invalidCommentAndNoSavedRecipe() {
        // Given
        val comment = " "
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.success(Unit)

        // When - then
        viewModel.onCommentChanged(comment)
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.onCommentDone()
        viewModel.state.value.editingComment shouldBe " "
        viewModel.state.value.canAddEditingComment.shouldBeFalse()
        viewModel.state.value.comments shouldContainInOrder listOf(
            "Comment 1",
            "Comment 2"
        )
        viewModel.state.value.navigation shouldBe Navigation.ForwardCreation
        coVerify { setCommentsUC(comments.sortedBy { it.ordinal }) }
    }

    @Test
    fun goForwardAndSaveState_when_onValidate_given_noError() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        coVerify { setCommentsUC(comments.sortedBy { it.ordinal }) }
    }

    @Test
    fun goForward_when_onValidate_given_error() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onValidate()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
    }

    @Test
    fun resetNavigation_when_onNavigationDone() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.failure(IllegalStateException())

        // When - then
        viewModel.onValidate()
        viewModel.state.value.navigation shouldBe Navigation.ForwardEditing
        viewModel.onNavigationDone()
        viewModel.state.value.navigation.shouldBeNull()
    }

    @Test
    fun goBackAndSaveState_when_onBack_given_noError() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
        coVerify { setCommentsUC(comments.sortedBy { it.ordinal }) }
    }

    @Test
    fun goBack_when_onBack_given_error() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onBack()

        // Then
        viewModel.state.value.navigation shouldBe Navigation.Back
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noError() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
        coVerify { setCommentsUC(comments.sortedBy { it.ordinal }) }
    }

    @Test
    fun goToPage_when_onSelectPage_given_error() {
        // Given
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.failure(IllegalStateException())

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, true)
    }

    @Test
    fun goToPageAndSaveState_when_onSelectPage_given_noRecipe() {
        // Given
        coEvery { isExistingUC(null) } returns Result.success(false)
        viewModel = EditRecipeCommentsViewModel(isExistingUC, getCommentsUC, setCommentsUC)
        coEvery { setCommentsUC(any()) } returns Result.success(Unit)

        // When
        viewModel.onSelectPage(PageType.Ingredients)

        // Then
        viewModel.state.value.navigation shouldBe Navigation.ToPage(PageType.Ingredients, false)
        coVerify { setCommentsUC(comments.sortedBy { it.ordinal }) }
    }
}
