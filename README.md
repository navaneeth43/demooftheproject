# 🏥 Health Card Management System

A modern, full-stack web application for managing patient health cards with a beautiful UI and comprehensive features.

## ✨ Features

### 🎨 Modern Frontend
- **Beautiful UI** with gradient backgrounds and glassmorphism effects
- **Responsive design** that works on all devices
- **Real-time search** and filtering capabilities
- **Interactive dashboard** with statistics
- **Smooth animations** and professional styling

### 📊 Dashboard Features
- **Statistics Overview** - Total cards, gender breakdown, average age
- **Smart Search** - Search by name, email, or phone number
- **Gender Filters** - Quick filtering by Male/Female/Other/All
- **Data Export** - Download health cards data as CSV
- **Bulk Operations** - Delete all cards with confirmation

### 🔧 CRUD Operations
- **Create** new health cards with comprehensive form validation
- **Read** all health cards with beautiful card layouts
- **Update** existing cards with edit functionality
- **Delete** individual cards or bulk delete all

### 🏗️ Backend API
- **RESTful API** built with Spring Boot
- **MySQL Database** integration with JPA/Hibernate
- **CORS enabled** for cross-origin requests
- **Automatic table creation** and management

## 🚀 Technology Stack

### Backend
- **Java 11+**
- **Spring Boot 2.7.0**
- **Spring Data JPA**
- **MySQL Database**
- **Maven** for dependency management

### Frontend
- **HTML5** with semantic markup
- **CSS3** with modern features (Grid, Flexbox, Animations)
- **Vanilla JavaScript** with ES6+ features
- **Font Awesome** icons
- **Responsive design** principles

## 📋 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cards` | Get all health cards |
| GET | `/api/card/{id}` | Get health card by ID |
| POST | `/api/save` | Create new health card |
| DELETE | `/api/delete/{id}` | Delete health card by ID |

## 🗄️ Database Schema

### Health Cards Table
```sql
CREATE TABLE health_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL
);
```

## 🛠️ Installation & Setup

### Prerequisites
- Java 11 or higher
- MySQL Server
- Maven 3.6+

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE healthcard_db;
```

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthcard_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
1. Clone the repository
2. Navigate to project directory
3. Run the application:
```bash
mvn spring-boot:run
```

4. Access the application:
   - **Frontend**: http://localhost:8080/
   - **API**: http://localhost:8080/api/cards

## 🎯 Usage

### Adding Health Cards
1. Fill out the form with patient information
2. Click "Save Health Card"
3. View the new card in the dashboard

### Managing Cards
- **Search**: Use the search bar to find specific patients
- **Filter**: Click gender filter buttons to view specific groups
- **Edit**: Click the edit button on any card to modify information
- **Delete**: Remove individual cards or use bulk delete

### Exporting Data
- Click "Export Data" to download all health cards as CSV
- Perfect for backup or external analysis

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/healthcard_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

## 🎨 UI Screenshots

The application features:
- Modern gradient backgrounds
- Professional card layouts
- Interactive statistics dashboard
- Responsive mobile design
- Smooth animations and transitions

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

Created with ❤️ for modern healthcare management.

---

**🏥 Health Card Management System** - Making healthcare data management simple and beautiful!
