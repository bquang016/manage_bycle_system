$(document).ready(function () {
    const deleteModalEl = $('#deleteConfirmModal');
    const deleteModal = deleteModalEl.length ? new bootstrap.Modal(deleteModalEl[0]) : null;
    const bikeNameToDeleteEl = $('#bike-name-to-delete');
    const deleteForm = $('#delete-form'); // The form inside the modal

    // When a delete button on the grid is clicked
    $('#bicycle-grid-container').on('click', '.btn-delete', function () {
        const bikeId = $(this).data('id');
        const bikeName = $(this).data('name');

        if (bikeId && bikeName !== undefined && deleteModal && deleteForm.length) {
            bikeNameToDeleteEl.text(`"${bikeName}"`);
            // Update the form's action attribute to include the correct ID
            const actionUrl = `/bikes/delete/${bikeId}`; // Adjust if your base path is different
            deleteForm.attr('action', actionUrl);
            // The modal is shown via data-bs-toggle attributes, no need for deleteModal.show() here
        } else {
            console.error("Could not get bike ID or Name for deletion, or modal elements not found.");
            // Optionally show a toast message for the error
        }
    });

    // Optional: Clear form action when modal is hidden to prevent accidental submission
    deleteModalEl?.on('hidden.bs.modal', function () {
        deleteForm?.attr('action', '#'); // Reset action
        bikeNameToDeleteEl?.text(''); // Clear name
    });

});