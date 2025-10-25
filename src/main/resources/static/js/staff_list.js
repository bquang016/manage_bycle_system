$(document).ready(function () {
    const deleteModalEl = $('#deleteConfirmModal');
    const deleteModal = deleteModalEl.length ? new bootstrap.Modal(deleteModalEl[0]) : null;
    const staffNameToDeleteEl = $('#staff-name-to-delete');
    const deleteForm = $('#delete-form'); // Form trong modal

    // Khi nút xóa trên bảng được click
    $('#staff-table-body').on('click', '.btn-delete', function () {
        const staffId = $(this).data('id');
        const staffName = $(this).data('name');

        if (staffId && staffName !== undefined && deleteModal && deleteForm.length) {
            staffNameToDeleteEl.text(`"${staffName}"`);
            // Cập nhật action của form xóa để POST đến đúng ID
            const actionUrl = `/staffs/delete/${staffId}`;
            deleteForm.attr('action', actionUrl);
            // Modal được hiển thị bằng data-bs-toggle
        } else {
            console.error("Không thể lấy ID hoặc Tên nhân viên để xóa.");
            // Có thể hiển thị toast lỗi ở đây
        }
    });

    // Tùy chọn: Reset form action khi modal ẩn đi
    deleteModalEl?.on('hidden.bs.modal', function () {
       deleteForm?.attr('action', '#'); // Reset action
       staffNameToDeleteEl?.text('');
    });
});