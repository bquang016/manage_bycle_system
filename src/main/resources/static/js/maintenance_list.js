$(document).ready(function () {
    const deleteModalEl = $('#deleteConfirmModal');
    const deleteModal = deleteModalEl.length ? new bootstrap.Modal(deleteModalEl[0]) : null;
    const maintNameToDeleteEl = $('#maint-name-to-delete'); // Đổi ID nếu cần
    const deleteForm = $('#delete-form');

    $('#maintenance-table-body').on('click', '.btn-delete', function () {
        const maintId = $(this).data('id');
        const maintName = $(this).data('name'); // Lấy tên mô tả từ data-name

        if (maintId && maintName !== undefined && deleteModal && deleteForm.length) {
            maintNameToDeleteEl.text(`${maintName}`); // Hiển thị mô tả
            const actionUrl = `/maintenances/delete/${maintId}`;
            deleteForm.attr('action', actionUrl);
        } else {
            console.error("Không thể lấy ID hoặc Mô tả bảo trì để xóa.");
        }
    });

    deleteModalEl?.on('hidden.bs.modal', function () {
        deleteForm?.attr('action', '#');
        maintNameToDeleteEl?.text('');
    });

    const searchTypeSelect = $('select[name="searchType"]');
    const searchInputs = $('.search-input'); // Class chung cho các input keyword

    function toggleSearchInput() {
        const selectedType = searchTypeSelect.val();
        searchInputs.hide().attr('name', ''); // Ẩn và xóa name tất cả
        if (selectedType === 'bikeId') {
            $('#search-bikeId').show().attr('name', 'keyword'); // Hiện input mã xe
        } else if (selectedType === 'date') {
            $('#search-date').show().attr('name', 'keyword'); // Hiện input ngày
        }
    }
    searchTypeSelect.on('change', toggleSearchInput);
    toggleSearchInput();
});