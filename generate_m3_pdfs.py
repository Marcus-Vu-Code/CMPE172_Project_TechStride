from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.lib.units import inch


def wireframes(path: str):
    c = canvas.Canvas(path, pagesize=letter)
    w, h = letter

    pages = [
        ("Home Page Wireframe", [
            ("Header: TechStride Scheduler", 1, 9.8, 6.5, 0.6),
            ("Nav links: Slots | Book | API", 1, 9.0, 6.5, 0.5),
            ("Main: Welcome + description", 1, 7.7, 6.5, 1.1),
            ("Buttons: View Slots / Book", 1, 6.2, 6.5, 0.9),
        ]),
        ("View Available Slots Wireframe", [
            ("Header", 1, 9.8, 6.5, 0.6),
            ("Table: Slot ID | Coach | Start | End | Status", 1, 6.0, 6.5, 3.6),
            ("Link: Book Appointment", 1, 5.2, 6.5, 0.5),
        ]),
        ("Book Appointment Form Wireframe", [
            ("Header", 1, 9.8, 6.5, 0.6),
            ("Form: Candidate ID", 1, 8.6, 6.5, 0.6),
            ("Form: Service ID", 1, 7.7, 6.5, 0.6),
            ("Form: Slot dropdown", 1, 6.8, 6.5, 0.8),
            ("Submit button", 1, 5.7, 6.5, 0.7),
        ]),
        ("Confirmation Page Wireframe", [
            ("Header", 1, 9.8, 6.5, 0.6),
            ("Message: Success / Failure", 1, 8.3, 6.5, 1.2),
            ("Links: Home | Slots | Appointments", 1, 7.1, 6.5, 0.7),
        ]),
    ]

    for title, boxes in pages:
        c.setFont("Helvetica-Bold", 16)
        c.drawString(1 * inch, h - 1 * inch, title)

        c.setFont("Helvetica", 11)
        c.drawString(1 * inch, h - 1.35 * inch, "(Simple mockup for M3 submission)")

        for label, x, y, bw, bh in boxes:
            # Convert from inches-ish coordinates
            px = x * inch
            py = y * inch
            c.rect(px, py, bw * inch, bh * inch)
            c.drawString(px + 0.15 * inch, py + bh * inch - 0.35 * inch, label)

        c.showPage()

    c.save()


def design_notes(path: str):
    c = canvas.Canvas(path, pagesize=letter)
    w, h = letter
    left = 1 * inch
    top = h - 1 * inch

    def draw_paragraph(text, y, font="Helvetica", size=11, leading=14):
        c.setFont(font, size)
        for line in text.split("\n"):
            c.drawString(left, y, line)
            y -= leading
        return y

    y = top
    c.setFont("Helvetica-Bold", 16)
    c.drawString(left, y, "M3_DesignNotes")
    y -= 22

    y = draw_paragraph(
        "System: TechStride Interview & Certification Scheduler\n"
        "Milestone: M3 – Web Interface + Application Skeleton\n"
        "", y
    )

    c.setFont("Helvetica-Bold", 13)
    c.drawString(left, y, "1) Chosen Pattern: Page Controller (within Spring MVC)")
    y -= 18
    y = draw_paragraph(
        "At the application design level, this project uses the Page Controller pattern: each page/route has\n"
        "a dedicated controller method responsible for handling the request and selecting the view.\n"
        "Examples:\n"
        "- GET /            -> HomeController returns home.html\n"
        "- GET /slots       -> SlotPageController returns slots.html\n"
        "- GET /appointments/book -> AppointmentPageController returns book.html\n"
        "\n"
        "Note: Spring Boot internally uses a Front Controller (DispatcherServlet) to route HTTP requests.\n"
        "Our code still cleanly demonstrates Page Controllers as the outward design for each page.",
        y
    )

    y -= 6
    c.setFont("Helvetica-Bold", 13)
    c.drawString(left, y, "2) Request Flow Through Layers")
    y -= 18
    y = draw_paragraph(
        "Example request: GET /slots\n"
        "1. Client sends HTTP request to the Spring Boot application.\n"
        "2. Spring MVC routes it to SlotPageController (Controller layer).\n"
        "3. The controller calls AppointmentService.listAvailableSlots() (Service layer).\n"
        "4. The service calls SlotRepository.findAvailableSlots() (Repository layer).\n"
        "5. The repository executes SQL via JdbcTemplate against the SQLite database (Database layer).\n"
        "6. The controller adds results to the model and returns the Thymeleaf view slots.html.\n"
        "\n"
        "This demonstrates the required layered enterprise architecture: Controller → Service → Repository → Database.",
        y
    )

    c.showPage()
    c.save()


if __name__ == "__main__":
    wireframes("wireframes.pdf")
    design_notes("M3_DesignNotes.pdf")
    print("Generated wireframes.pdf and M3_DesignNotes.pdf")
